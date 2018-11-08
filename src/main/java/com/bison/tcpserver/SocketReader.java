package com.bison.tcpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketReader {
    private static final Logger log = LoggerFactory.getLogger(SocketReader.class);
    public static volatile SocketReader instance = null;

    private SocketReader() {
    }

    public static synchronized SocketReader getInstance() {
        synchronized (SocketReader.class) {
            if (instance == null)
                instance = new SocketReader();
        }
        return instance;
    }

    public void read(Socket socket) {
        startReader(socket);
    }

    private void startReader(Socket socket) {
        try (DataInputStream is = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
            boolean continueRead = true;
            String tracker = "";
            while (continueRead) {
                try {
                    byte[] bs = new byte[is.available()];
                    //  if (is.read() != -1) {
                    is.read(bs);
                    StringBuilder sb = new StringBuilder();
                    for (byte b : bs) {
                        char c = (char) b;
                        sb.append(c);
                    }
                    String res = sb.toString();
                    if (res != null && !"".equalsIgnoreCase(res)) {
                        tracker = getClientId(res);
                        if (SocketManager.getInstance().getSocketInfo(tracker) == null) {
                            SocketManager.getInstance().addSocketInfo(tracker, socket);
                        }
                        // add into database or further process it 
                        log.info("Received Message = " + res);
                    }
                    //  }
                } catch (Exception e) {
                    e.printStackTrace();
                    continueRead = false;
                    SocketManager.getInstance().removeSocketInfo(tracker);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            log.error("error while reading socket.,", e);
        }
    }

    private String getClientId(String clientRequestMessage) {
        log.info("clientId: " + clientRequestMessage);
        if (clientRequestMessage != null && !"".equalsIgnoreCase(clientRequestMessage)) {
            return clientRequestMessage.substring(clientRequestMessage.indexOf("#") + 1, clientRequestMessage.indexOf(","));
        }
        return null;
    }
}
