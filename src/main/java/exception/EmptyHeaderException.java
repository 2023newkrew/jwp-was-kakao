package exception;

public class EmptyHeaderException extends RuntimeException {
    public EmptyHeaderException(String message) {
        super(message);
    }
}
