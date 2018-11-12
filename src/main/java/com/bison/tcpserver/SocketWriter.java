package com.bison.tcpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SocketWriter {
    private static final Logger log = LoggerFactory.getLogger(SocketWriter.class);
    public static volatile SocketWriter instance = null;

    public static synchronized SocketWriter getInstance() {
        synchronized (SocketWriter.class) {
            if (instance == null)
                instance = new SocketWriter();
        }
        return instance;
    }

    private SocketWriter() {
    }

    private List<Socket> getSocket(String trackerId) {
        Map<String, Socket> socketMap = SocketManager.getInstance().getAllSockets();

        return socketMap.entrySet().stream().filter(map -> trackerId.equalsIgnoreCase(map.getKey()))
                .map(map -> map.getValue()).collect(Collectors.toList());

    }

    public void sendCommand(String tracker, String command) {
        try {
            if (tracker == null || tracker == "") {
                log.info("WTF where is tracker");
            }
            List<Socket> socketList = getSocket(tracker);
            if (socketList.size() == 0) {
                log.warn("No available socket for this tracker {}", tracker);
                return;
            }
            if (socketList.size() > 1) {
                log.warn("more than 1 active socket of same device. Close old one");
            }
            DataOutputStream os = new DataOutputStream(new BufferedOutputStream(socketList.get(0).getOutputStream()));
            log.info("start sending command " + command);
            if (command != null || command != "") {
                command = ":" + command + "#";
                // Sending command to Client
                byte[] byteData = command.getBytes();
                if (byteData == null && byteData.length == 0) {
                    return;
                }
                os.write(byteData);
                    os.flush();
                    log.info("write done!");

            }
        } catch (IOException e) {
            log.error("error while writing socket.,", e);
            SocketManager.getInstance().removeSocketInfo(tracker);
        }
    }
}
