package org.lwjgl.openal;

public class OpenALException extends RuntimeException {
    public OpenALException() {
        super();
    }

    public OpenALException(String message) {
        super(message);
    }

    public OpenALException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenALException(Throwable cause) {
        super(cause);
    }

    public OpenALException(int errorCode) {
        super("OpenAL error: " + errorCode);
    }
}
