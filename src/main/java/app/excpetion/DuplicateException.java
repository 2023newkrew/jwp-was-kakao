package app.excpetion;

import infra.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DuplicateException extends BaseException {
    private static final String EXPECTED = "id가 중복일 수 없습니다.";
    private static final String ACTUAL = "id가 중복입니다.";

    public DuplicateException(String context) {
        super(ACTUAL, EXPECTED, context, HttpStatus.BAD_REQUEST.value());
    }
}
