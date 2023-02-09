package http;

import exception.BadRequestException;

import java.util.Arrays;

public enum HttpRequestHeader {
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    CONNECTION("Connection"),
    ACCEPT("Accept"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    COOKIE("Cookie"),
    USER_AGENT("User-Agent"),
    HOST("Host"),
    REFERER("Referer"),
    CACHE_CONTROL("Cache-Control");

    private final String value;

    HttpRequestHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static HttpRequestHeader of(String value) {
        return Arrays.stream(values())
                .filter(header -> value.equals(header.getValue()))
                .findAny()
                .orElseThrow(BadRequestException::new);
    }
}

