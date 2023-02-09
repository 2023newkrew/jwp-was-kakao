package controller;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
