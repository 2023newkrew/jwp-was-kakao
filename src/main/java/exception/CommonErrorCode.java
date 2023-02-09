package exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {

    DUPLICATE_ENTITY(HttpStatus.BAD_REQUEST, "중복된 항목이 있습니다."),

    NOT_EXIST_ENTITY(HttpStatus.BAD_REQUEST, "찾을 수 없는 항목입니다."),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    CommonErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
