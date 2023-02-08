package webserver.exceptions;

public class HandlerNotFoundException extends Exception {
    public HandlerNotFoundException() {
        super("No Mapping Handler for Request");
    }
}
