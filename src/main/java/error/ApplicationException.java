package error;

public class ApplicationException extends RuntimeException {

    private final ErrorType errorType;

    public ApplicationException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

}
