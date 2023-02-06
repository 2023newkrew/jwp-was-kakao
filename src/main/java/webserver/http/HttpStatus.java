package webserver.http;

public enum HttpStatus {
    OK(200),
    FOUND(302),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405);

    private final int code;

    HttpStatus(final int code) {
        this.code = code;
    }
}
