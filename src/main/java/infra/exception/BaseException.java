package infra.exception;

public abstract class BaseException extends RuntimeException{

    private final int statusCode;

    public BaseException(String actual, String expected, String context, int statusCode) {
        super(makeMessage(actual, expected, context));
        this.statusCode = statusCode;
    }

    public BaseException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    private static String makeMessage(String actual, String expected, String context) {
        return String.format("\r\n"
                + "ACTUAL   : %s \r\n"
                + "EXPECTED : %s \r\n"
                + "CONTEXT  : %s \r\n", actual, expected, context);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
