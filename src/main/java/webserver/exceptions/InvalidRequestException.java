package webserver.exceptions;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }
}
