package support;

import common.HttpStatus;

public class UnknownParameterException extends CustomException {
    public UnknownParameterException() {
        super(HttpStatus.BAD_REQUEST, "정의되지 않은 파라미터가 포함되어 있습니다.");
    }
}
