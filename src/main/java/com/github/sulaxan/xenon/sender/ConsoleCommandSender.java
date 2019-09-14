package com.github.sulaxan.xenon.sender;

public class ConsoleCommandSender implements CommandSender {

    /**
     * The instance of the default console command sender. This implementation
     * of {@link CommandSender} prints to the default output and error streams.
     */
    public static final ConsoleCommandSender INSTANCE = new ConsoleCommandSender();

    private ConsoleCommandSender() {
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void sendError(String error) {
        System.err.println(error);
    }
}
