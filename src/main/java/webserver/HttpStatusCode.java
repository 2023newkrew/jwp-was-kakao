package webserver;

import lombok.Getter;

@Getter
public enum HttpStatusCode {
    OK(200, "OK"),
    FOUND(302, "Found");

    private final int statusCode;
    private final String message;

    HttpStatusCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
