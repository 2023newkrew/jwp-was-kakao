package infra.exception;

import org.springframework.http.HttpStatus;

public class NullOrBlankFieldException extends BaseException {
    private static final String EXPECTED = "해당 Field는 Null 또는 Blank일 수 없습니다.";

    public NullOrBlankFieldException(String actual, String context) {
        super(actual, EXPECTED, context, HttpStatus.BAD_REQUEST.value());
    }
}
