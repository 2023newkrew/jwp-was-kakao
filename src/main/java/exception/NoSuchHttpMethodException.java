package exception;

public class NoSuchHttpMethodException extends RuntimeException {
    public NoSuchHttpMethodException(String message) {
        super(message);
    }
}
