package webserver.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("No Resource Exists at Requested Path");
    }
}
