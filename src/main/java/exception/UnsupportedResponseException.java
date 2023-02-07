package exception;

public class UnsupportedResponseException extends RuntimeException {
    public UnsupportedResponseException(String message) {
        super(message);
    }
}
