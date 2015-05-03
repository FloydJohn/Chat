package com.floydjohn;

import java.util.regex.Matcher;

public interface Command {
    public void execute(Channel channel, Matcher match);
}