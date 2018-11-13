package com.bison.tcpclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * TCP Socket Implementation
 */
public class TCPClient2 {

    public static void main(String[] args) throws Exception {
        System.out.println("Client Started...");

        Socket socket = new Socket("localhost", 9090);
        DataInputStream is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        // Create Client Request
        String request = "#867688031658498,CMD-D,#";
        // Send Client Request to Server
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        send(os, request.getBytes());
        System.out.println("Data sent to Server ; Message = " + request);

        try {
            while (true) {
                int count = is != null ? is.available() : 0;
                if (count > 0) {
                    byte[] bs = new byte[count];
                    is.read(bs);
                    StringBuilder bufferedReader = new StringBuilder();
                    for (byte b : bs) {
                        char c = (char) b;
                        bufferedReader.append(c);
                    }
                    if (bufferedReader != null || bufferedReader.toString() != "") {
                        System.out.println(bufferedReader.toString());
                        request = request.concat(bufferedReader.toString());
                        send(os, request.getBytes());
                        System.out.println("Data sent to Server ; Message = " + request);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    } 

    /**
     * Method used to Send Request to Server
     */
    public static void send(DataOutputStream os, byte[] byteData) throws Exception {
        try {
            os.write(byteData);
            os.flush();
        } catch (Exception exception) {
            throw exception;
        }
    }
}
