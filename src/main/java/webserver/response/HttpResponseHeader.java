package webserver.response;

public enum HttpResponseHeader {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    LOCATION("Location"),
    ;

    private final String header;

    HttpResponseHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
