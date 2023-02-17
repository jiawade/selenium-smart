package io.smart.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super();
    }

    public FileNotFoundException(String msg) {
        super(msg);
    }
}
