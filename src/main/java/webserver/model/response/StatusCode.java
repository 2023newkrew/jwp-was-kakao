package webserver.model.response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatusCode {
    OK(200, "OK"),
    FOUND(302, "FOUND"),
    NOT_FOUND(404, "NOT FOUND"),
    BAD_REQUEST(400, "BAD REQUEST"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR");

    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
