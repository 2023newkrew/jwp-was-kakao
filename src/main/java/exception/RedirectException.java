package exception;

public class RedirectException extends RuntimeException {
    public RedirectException(String location) {
        super("Redirect to " + location);
    }
}
