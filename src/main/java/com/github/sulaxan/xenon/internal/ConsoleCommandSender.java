package com.github.sulaxan.xenon.internal;

import com.github.sulaxan.xenon.sender.CommandSender;

public class ConsoleCommandSender implements CommandSender {

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void sendError(String error) {
        System.err.println(error);
    }
}
