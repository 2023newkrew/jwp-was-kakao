package support;

import common.HttpStatus;

public class InvalidParameterException extends CustomException {
    public InvalidParameterException(String key) {
        super(HttpStatus.BAD_REQUEST, "파라미터 " + key + "가 유효하지 않은 값입니다.");
    }
}
