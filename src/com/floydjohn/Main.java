package com.floydjohn;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.net.BindException;

public class Main {
    public static int DEFAULT_PORT = 1234;

    public static void main(String[] args) throws IOException{
        int port = DEFAULT_PORT;
        Console.clearScreen();
        switch(args.length) {
            case 1:
                if(args[0].equals("help") || args[0].equals("?")) Console.help();
                if(Integer.parseInt(args[0]) > 0) port = Integer.parseInt(args[0]);
                Console.initLog();
                break;
            case 2:
                if(Integer.parseInt(args[0]) > 0) port = Integer.parseInt(args[0]);
                if(Boolean.getBoolean(args[1])) Console.initLog();
                break;
            default:
                Console.initLog();
                break;
        }
        ChannelsManager manager = new ChannelsManager();
        DefaultProtocol protocol = new DefaultProtocol(manager);
        try {
            Server server = new Server(port, manager);
            server.start();
            Console.printlnPure("Running!", Console.ANSI_GREEN);
            Console.printlnPure("Internal IP : "+IpUtils.getInternalIP(), Console.ANSI_CYAN);
            Console.printlnPure("External IP : "+IpUtils.getExternalIP(), Console.ANSI_CYAN);
            Console.printlnPure("Port        : "+String.valueOf(port), Console.ANSI_CYAN);
            Console.printlnPure("Log File    : "+Console.getFileName(), Console.ANSI_CYAN);
        } catch (BindException e){
            Console.printlnPure("Current port is busy. Try another one!", Console.ANSI_RED);
        }
    }
}
