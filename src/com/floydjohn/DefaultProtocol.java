package com.floydjohn;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultProtocol extends Protocol {

    private Map<String, Command> commands;

    {
        commands = new HashMap<String, Command>();
        commands.put("list", new List());
        commands.put("msg",  new Msg());
        commands.put("time", new Time());
        commands.put("login", new Login());
        commands.put("quit", new Quit());
    }

    public DefaultProtocol(ChannelsManager manager) {
        super(manager);
    }

    public void parserMessage(Channel ch, String str) {
        Console.printlnPure("Server <- "+ch.getName()+" : " + str, Console.ANSI_YELLOW);
        while(str.charAt(0)!='/' && str.length() > 0){
            StringBuilder sb = new StringBuilder(str);
            sb.deleteCharAt(0);
            str = sb.toString();
        }
        if(str.charAt(0)!='/') {
            ch.sendlnColored("NO_SLASH_ERROR",Console.ANSI_RED);
        } else {
            Pattern pattern = Pattern.compile("\\/([^\\s]+)\\s(.*)");
            Matcher match = pattern.matcher(str);
            match.find();
            String command = match.group(1);
            if (commands.containsKey(command)) {
                Command cmd = commands.get(command);
                cmd.execute(ch, match);
            } else {
                ch.sendlnColored("WRONG_COMMAND_ERROR",Console.ANSI_RED);
            }
        }
    }
    /* Invia la stringa msg a tutti i client, il flag mysend
    indica se inviarlo anche al mittente */
    protected void broadcast(String name, String msg, boolean mysend) {
        Set <String> set =  manager.getAllName();
        for(String str : set) {
            if(!str.equalsIgnoreCase(name) || mysend) {
                Channel channel = manager.getChannel(str);
                if(channel.isLogin()) channel.sendln(msg);
            }
        }
    }

    protected void broadcastColored(String name, String msg, boolean mysend, String color) {
        broadcast(name,color+msg+Console.ANSI_RESET,mysend);
    }

    protected void broadcastMessage(String user, String msg) {
        Set <String> set =  manager.getAllName();
        for(String str : set) {
                Channel channel = manager.getChannel(str);
                if(channel.isLogin()) {
                    channel.sendColored("["+user+"] ",Console.ANSI_PURPLE);
                    channel.sendlnColored(msg,Console.ANSI_CYAN);
                }
        }
    }

    public void startMessage(Channel ch) {
        ch.sendlnColored("HELLESCAPE_GAME_SERVER", Console.ANSI_BLUE);
    }

    private class Login implements Command {
        public void execute(Channel channel, Matcher match) {
            if(!channel.isLogin()) {
                String name= match.group(2);
                if (name.length()==0) channel.sendlnColored("NO_NAME_ERROR",Console.ANSI_RED);
                else if(manager.addChannel(name.toLowerCase(), channel)) {
                    broadcastColored(name, "NEW_USER:" + name, false, Console.ANSI_GREEN);
                    channel.setLogin(true);
                    channel.sendlnColored("LOGIN_OK",Console.ANSI_GREEN);
                } else {
                    channel.sendlnColored("USERNAME_ALREADY_EXISTING_ERROR",Console.ANSI_RED);
                }
            } else {
                channel.sendlnColored("ALREADY_LOGGED_ERROR",Console.ANSI_RED);
            }
        }
    }

    private class Msg implements Command {
        public void execute(Channel channel, Matcher match) {
            if (channel.isLogin()) {
                broadcastMessage(channel.getName(),match.group(2));
                Console.printlnPure("[" + channel.getName() + "] " + match.group(2), Console.ANSI_BLUE);
            } else {
                channel.sendlnColored("NO_LOGIN_ERROR",Console.ANSI_RED);
            }
        }
    }

    private class Time implements Command {
        public void execute(Channel channel, Matcher match) {
            channel.sendlnColored(new Date().toString(),Console.ANSI_YELLOW);
        }
    }

    private class Quit implements Command {
        public void execute(Channel channel, Matcher match) {
            channel.sendlnColored("QUIT_OK",Console.ANSI_GREEN);
            broadcastColored(channel.getName().toLowerCase(),
                    "USER_QUIT", false,Console.ANSI_RED);
            manager.removeChannel(channel.getName().toLowerCase());
            channel.closeChannel();
        }
    }

    private class List implements Command {
        public void execute(Channel channel, Matcher match) {
            Set<String> names = manager.getAllName();
            if (names == null) {
                channel.sendlnColored("NO_USER",Console.ANSI_RED);
            } else {
                for ( String name : names) channel.sendlnColored(name,Console.ANSI_PURPLE);
            }
        }
    }
}