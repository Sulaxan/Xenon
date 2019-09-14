package com.github.sulaxan.xenon.exception;

public class CommandRuntimeException extends RuntimeException {

    public CommandRuntimeException() {
    }

    public CommandRuntimeException(String message) {
        super(message);
    }

    public CommandRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandRuntimeException(Throwable cause) {
        super(cause);
    }

    public CommandRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
