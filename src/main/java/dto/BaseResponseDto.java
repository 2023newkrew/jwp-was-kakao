package dto;

import webserver.StatusCode;

public class BaseResponseDto {
    private final StatusCode statusCode;
    private final String body;

    public BaseResponseDto(StatusCode statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}

