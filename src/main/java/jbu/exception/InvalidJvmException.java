package jbu.exception;

public class InvalidJvmException extends RuntimeException {
    public InvalidJvmException(String message) {
        super(message);
    }

    public InvalidJvmException(String message, Throwable cause) {
        super(message, cause);
    }
}
