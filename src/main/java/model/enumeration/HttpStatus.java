package model.enumeration;

public enum HttpStatus {
    OK(200, "HTTP/1.1 200 OK \r\n"),
    FOUND(302, "HTTP/1.1 302 Found \r\n"),
    NOT_FOUND(404, "HTTP/1.1 404 Not Found \r\n"),

    ;


    private final int statusCode;
    private final String statusLine;

    HttpStatus(int statusCode, String statusLine) {
        this.statusCode = statusCode;
        this.statusLine = statusLine;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusLine() {
        return statusLine;
    }
}
