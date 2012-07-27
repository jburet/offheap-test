package jbu.exception;

public class CannotDeserializeException extends Exception {
    public CannotDeserializeException(String message) {
        super(message);
    }

    public CannotDeserializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
