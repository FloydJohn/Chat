package com.floydjohn;

import java.io.IOException;
import java.net.*;

public class Server extends Thread {

    private int port;
    private ServerSocket server;
    private ChannelsManager manager;

    public Server(int port, ChannelsManager manager) throws IOException {
        this.port = port;
        this.manager = manager;
        server = new ServerSocket(port);
    }

    public Server(int port) throws IOException {
        this(port, new ChannelsManager());
    }

    public int getPort() {
        return port;
    }

    public void run() {
        try {
            while(true) {
                Socket socket = server.accept();//Chiamata bloccante
                manager.initialite(socket);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}