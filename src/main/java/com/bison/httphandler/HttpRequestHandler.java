package com.bison.httphandler;


import com.bison.tcpserver.SocketWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequestHandler {
    public static volatile HttpRequestHandler instance = null;
    Map<String, RequestLocker> requestContainer = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    public static synchronized HttpRequestHandler getInstance() {
        synchronized (HttpRequestHandler.class) {
            if (instance == null)
                instance = new HttpRequestHandler();
        }
        return instance;
    }

    private HttpRequestHandler() {
    }

    public String runCommand(String trackerId, String cmd) {
        logger.info("Placing call for " + trackerId);
        SocketWriter.getInstance().sendCommand(trackerId, cmd);

        logger.info("Going into sleep for " + trackerId);
        synchronized (trackerId) {
            try {
                requestContainer.put(trackerId, new RequestLocker(trackerId, null));
                trackerId.wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        RequestLocker after = requestContainer.remove(trackerId);
        return (String) after.response;
    }

    public void setCommandResponse(String id, String res) {
        if (requestContainer.containsKey(id)) {
            RequestLocker result = requestContainer.get(id);
            synchronized (result.trackerId) {
                result.response = "Result for " + id;
                result.trackerId.notify();
            }
        } else {
            logger.warn("Skipping a unknow response from tracker at httpsRequestHandler {}, respone:{}", id, res);
        }
    }
}
