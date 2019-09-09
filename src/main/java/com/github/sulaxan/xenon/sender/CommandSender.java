package com.github.sulaxan.xenon.sender;

public interface CommandSender {

    void sendMessage(String message);

    void sendError(String error);
}
