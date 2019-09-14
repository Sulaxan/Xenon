package com.github.sulaxan.xenon.sender;

/**
 * Represents a user issuing a command.
 */
public interface CommandSender {

    /**
     * Used to send some information to a user.
     *
     * @param message The message to send.
     */
    void sendMessage(String message);

    /**
     * Used to send some error information to a user.
     *
     * @param error The error message to send.
     */
    void sendError(String error);
}
