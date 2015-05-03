package com.floydjohn;

public abstract class Protocol {
    protected ChannelsManager manager;
    public Protocol(ChannelsManager manager) {
        this.manager = manager;
        manager.setProtocol(this);
    }
    public abstract void startMessage(Channel ch);
    public abstract void parserMessage(Channel channel, String msg);
}