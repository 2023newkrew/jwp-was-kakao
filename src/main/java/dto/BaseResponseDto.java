package dto;

import webserver.StatusCode;

public class BaseResponseDto {
    private final StatusCode statusCode;
    private final String body;
    private final String contentType;

    public BaseResponseDto(StatusCode statusCode, String body, String contentType) {
        this.statusCode = statusCode;
        this.body = body;
        this.contentType = contentType;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }
}

