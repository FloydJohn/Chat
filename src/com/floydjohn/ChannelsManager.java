package com.floydjohn;

import java.net.Socket;
import java.util.*;

public class ChannelsManager {

    protected Map<String, Channel> channels;
    protected Protocol protocol = null;

    public ChannelsManager() {
        channels = new HashMap<String, Channel>();
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public void initialite(Socket socket) {
        Channel channel = new Channel(this, socket);
        channel.start();
        protocol.startMessage(channel);
    }

    public synchronized boolean addChannel(String name,Channel channel) {
        if(!channels.containsKey(name)) {
            channels.put(name, channel);
            channels.get(name).setName(name);
            return true;
        } else return false;
    }

    public synchronized void processMessage(Channel ch, String str) {
        protocol.parserMessage(ch, str);
    }

    public synchronized void removeChannel(String name) {
        if(channels.containsKey(name)) {
            Channel ch = channels.remove(name);
            ch.interrupt();
        }
    }

    public synchronized Set<String> getAllName() {
        return channels.keySet();
    }

    public synchronized Channel getChannel(String name) {
        if(channels.containsKey(name))
            return channels.get(name);
        else return null;
    }
}