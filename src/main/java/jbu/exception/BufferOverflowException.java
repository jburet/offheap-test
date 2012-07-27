package jbu.exception;

public class BufferOverflowException extends RuntimeException {

    public BufferOverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public BufferOverflowException(String message) {
        super(message);
    }
}
