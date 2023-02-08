package webserver.exceptions;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
        super("Invalid Request Format");
    }
}
