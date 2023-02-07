package webserver.response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatusCode {
    OK(200, "OK"),
    FOUND(302, "FOUND");

    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
