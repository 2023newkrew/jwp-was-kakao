package webserver;

public enum HttpHeader {
    CONTENT_LENGTH("Content-Length"),
    LOCATION("Location");
    private final String value;
    HttpHeader(final String value) {
            this.value = value;
        }
    public String value() {
            return value;
    }
}
