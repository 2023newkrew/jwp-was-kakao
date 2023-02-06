package http;

public enum HttpHeaders {
    HOST("Host"),
    ACCEPT("ACCEPT"),
    CONNECTION("CONNECTION"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    LOCATION("Location")
    ;

    private final String header;

    HttpHeaders(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
