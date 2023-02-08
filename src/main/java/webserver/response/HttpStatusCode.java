package webserver.response;

import lombok.Getter;

@Getter
public enum HttpStatusCode {
    OK(200, "OK"),
    FOUND(302, "Found"),
    NOT_FOUND(404, "Not Found");

    private final int statusCode;
    private final String message;

    HttpStatusCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
