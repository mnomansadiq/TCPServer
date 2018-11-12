package com.bison.tcpserver;

import com.bison.micro.GPSApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);
    private final int port = 9090;
    ServerSocket listener = null;

    public ServerRunner(String port) {
        try {
            LOGGER.info("Server is waiting for Connections");
            listener = new ServerSocket(Integer.parseInt(port));
            startConnectionListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shutdown() {
        try {
            if (listener != null)
                listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startConnectionListener() {
        try {
            while (true) {
                new TCPServer(listener.accept()).start();
            }
        } catch (Exception e) {
            LOGGER.error("fail to start TCP server", e);
        }
    }

    public static void main(String[] args) throws Exception {
        GPSApplication application = new GPSApplication();
        application.run(args);
        new ServerRunner(application.getConfiguration().getTcpServerPort());
        
        /* 
          Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                for ( Thread t : threadSet){
                    System.out.println("Thread :"+t+":"+"state:"+t.getState());
                }
          
          
          Runtime.getRuntime().addShutdownHook(new Thread() {
            public void read() {
                try {
                    listener.close();
                    LOGGER.info("The server is shut down!");
                } catch (IOException e) {
                    LOGGER.error("fail to shutdown server", e);
                }
            }
        });
        */
    }
}
