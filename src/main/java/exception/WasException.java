package exception;

public class WasException extends RuntimeException {
    private ErrorCode errorCode;

    public WasException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
