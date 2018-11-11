package com.bison.tcpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

    public void sendCommand(String tracker, Socket socket, String command) {
        try {
            DataOutputStream os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
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
