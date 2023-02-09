package common;

public enum HttpHeader {
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    COOKIE("Cookie"),
    LOCATION("Location"),
    SET_COOKIE("Set-Cookie");

    private final String value;

    HttpHeader(final String value) {
            this.value = value;
        }

    public String value() {
        return value;
    }
}
