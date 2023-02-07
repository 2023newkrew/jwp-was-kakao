package webserver.exception;

public class BadParameterException extends RuntimeException{

    public BadParameterException(final String message) {
        super(message);
    }
}
