package exception;

import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {

    INVALID_CREDENTIAL(HttpStatus.UNAUTHORIZED, "아이디 혹은 비밀번호가 잘못됐습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "수행 권한이 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    AuthErrorCode(HttpStatus httpStatus, String message) {
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
