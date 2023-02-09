package http;

import java.util.Arrays;

public enum HttpResponseHeader {
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_ENCODING("Content-Encoding"),
    KEEP_ALIVE("Keep-Alive"),
    SET_COOKIE("Set-Cookie"),
    CONNECTION("Connection"),
    CACHE_CONTROL("Cache-Control"),
    LOCATION("Location");

    private final String value;

    HttpResponseHeader(String value) {
        this.value = value;
    }

    public static HttpResponseHeader of(String value) {
        return Arrays.stream(values())
                .filter(header -> value.equals(header.value))
                .findAny()
                .orElse(null);
    }

    public String toString() {
        return value;
    }
}
