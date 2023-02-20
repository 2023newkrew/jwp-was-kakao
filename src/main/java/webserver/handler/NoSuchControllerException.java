package webserver.handler;

public class NoSuchControllerException extends RuntimeException {

    public NoSuchControllerException(String message) {
        super(message);
    }
}
