package com.bison.tcpserver;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketManager {
    private HashMap<String,Socket> socketInfo = new HashMap();
    public static volatile SocketManager instance = null;

    private SocketManager() {
    }

    public static synchronized SocketManager getInstance() {
        synchronized (SocketManager.class) {
            if (instance == null)
                instance = new SocketManager();
        }
        return instance;
    }

    public void addSocketInfo(String trackerId, Socket socket) {
        socketInfo.put(trackerId, socket);
    }

    public Socket getSocketInfo(String trackerId) {
        return socketInfo.containsKey(trackerId) ? (Socket) socketInfo.get(trackerId) : null;
    }

    public void removeSocketInfo(String trackerId) {
        if (socketInfo.containsKey(trackerId)) {
            socketInfo.remove(trackerId);
        }
    }

    public Map getAllSockets() {
        return socketInfo;
    }
}
