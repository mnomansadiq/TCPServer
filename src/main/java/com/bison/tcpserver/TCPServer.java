package com.bison.tcpserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

/**
 * TCP Server Socket Implementation
 */
public class TCPServer extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(TCPServer.class);
    private Socket socket;

    public TCPServer(Socket socket) {
        this.socket = socket;
        LOGGER.info("New connection with client# " + " at " + socket);
    }

    @Override
    public void run() {
        try {
            SocketReader.getInstance().read(this.socket);
        } catch (Exception e) {
            LOGGER.error("error while stating reading and writing thread", e);
        }
    }

}
