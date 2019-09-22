package com.github.sulaxan.xenon.exception;

/**
 * Represents a runtime exception thrown when a command cannot be found
 * when a user issues a command.
 */
public class CommandNotFoundException extends CommandRuntimeException {

    public CommandNotFoundException() {
    }

    public CommandNotFoundException(String message) {
        super(message);
    }

    public CommandNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandNotFoundException(Throwable cause) {
        super(cause);
    }

    public CommandNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
