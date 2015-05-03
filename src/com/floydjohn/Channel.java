package com.floydjohn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Channel extends Thread{

    private final int LF = 10;
    private ChannelsManager manager;
    private Socket socket;
    private boolean login = false;

    public Channel(ChannelsManager manager, Socket socket) {
        this.manager = manager;
        this.socket = socket;
    }

    public void sendln(String msg) {
        try {
            msg+="\r\n";
            OutputStream outStream = socket.getOutputStream();
            outStream.write(msg.getBytes());
            outStream.flush();
            Console.printlnPure("Server -> " + this.getName() + " : " + msg, Console.ANSI_CYAN);
        }
        catch(IOException ex) {}
    }

    public void send(String msg) {
        try {
            OutputStream outStream = socket.getOutputStream();
            outStream.write(msg.getBytes());
            outStream.flush();
            Console.printlnPure("Server -> " + this.getName() + " : " + msg, Console.ANSI_CYAN);
        }
        catch(IOException ex) {}
    }

    public void sendlnColored(String msg, String color) {
        try {
            msg+="\r\n"+Console.ANSI_RESET;
            color+=msg;
            OutputStream outStream = socket.getOutputStream();
            outStream.write(color.getBytes());
            outStream.flush();
            Console.printlnPure("Server -> " + this.getName() + " : " + msg, Console.ANSI_CYAN);
        }
        catch(IOException ex) {}
    }

    public void sendColored(String msg,String color) {
        send(color+msg+Console.ANSI_RESET);
    }

    public String receive() throws IOException {
        String line = "";
        InputStream inStream = socket.getInputStream();
        int read = inStream.read();
        while (read!=LF && read > -1) {
            line+=String.valueOf((char)read);
            read = inStream.read();
        }
        if (read==-1) return null;
        line+=String.valueOf((char)read);
        return line;
    }

    public void closeChannel() {
        try {
            interrupt();
            socket.close();
        } catch(Exception e) {}
    }

    public void run() {
        try {
            while(true) {
                String msg = receive();
                if(msg!=null)
                    manager.processMessage(this, msg);
                else {
                    closeChannel();
                    break;
                }
            }
        }
        catch(Exception ex) {}
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }


}
