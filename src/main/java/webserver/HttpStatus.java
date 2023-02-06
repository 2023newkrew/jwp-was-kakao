package webserver;

public enum HttpStatus {
    OK(200, "OK"),
    FOUND(302, "Found"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error" );

    public final int code;
    public final String message;

    HttpStatus(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
