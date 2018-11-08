package com.bison.tcpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);
    private final int port = 9090;
    ServerSocket listener = null;

    public ServerRunner() {
        try {
            LOGGER.info("Server is waiting for Connections");
            listener = new ServerSocket(port);
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

        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("scheduler is running...");
                String[] commands = {"123456F", "123456G"};
                Map<String, Socket> socketsList = SocketManager.getInstance().getAllSockets();
                socketsList.forEach((key, socket) -> {
                    SocketWriter.getInstance().sendCommand(key, socket, commands[new Random().nextInt(commands.length)]);
                });

                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                for ( Thread t : threadSet){
                    System.out.println("Thread :"+t+":"+"state:"+t.getState());
                }
            }
            
        }, 10, 10, TimeUnit.SECONDS);


        new ServerRunner();

        /*   Runtime.getRuntime().addShutdownHook(new Thread() {
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
