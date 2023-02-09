package exception;

import type.HttpStatusCode;

public enum ErrorCode {
    QUERY_STRING_NOT_EXIST("쿼리스트링이 존재하지 않습니다."),
    UNKNOWN_ERROR("서버 내부 오류가 발생했습니다.", HttpStatusCode.INTERNAL_SERVER_ERROR),
    CAN_NOT_READ_DATA("요청을 읽는 도중 오류가 발생했습니다", HttpStatusCode.BAD_REQUEST),
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

    public Integer getHttpStatusCode() {
        return httpStatusCode.getCode();
    }
}
