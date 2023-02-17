package io.smart.exception;

public class NotDirectoryException extends RuntimeException {
    public NotDirectoryException() {
        super();
    }

    public NotDirectoryException(String msg) {
        super(msg);
    }
}
