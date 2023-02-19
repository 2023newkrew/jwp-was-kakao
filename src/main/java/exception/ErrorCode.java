package exception;

import type.HttpStatusCode;

public enum ErrorCode {
    USER_NOT_EXIST("User does not exist.", HttpStatusCode.BAD_REQUEST),
    UNKNOWN_ERROR("Internal server error is occured.", HttpStatusCode.INTERNAL_SERVER_ERROR),
    CAN_NOT_READ_DATA("Can't read data.", HttpStatusCode.BAD_REQUEST),
    BAD_REQUEST("Your request is invalid.", HttpStatusCode.BAD_REQUEST),
    CAN_NOT_WRITE_DATA("Can't response.", HttpStatusCode.INTERNAL_SERVER_ERROR),
    ;

    private String description;
    private HttpStatusCode httpStatusCode;

    ErrorCode(String description, HttpStatusCode httpStatusCode) {
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

    ErrorCode(String description) {
        this.description = description;
        this.httpStatusCode = null;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}
