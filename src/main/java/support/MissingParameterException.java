package support;

import common.HttpStatus;

public class MissingParameterException extends CustomException {
    public MissingParameterException(String key) {
        super(HttpStatus.BAD_REQUEST, "요구되는 파라미터 " + key+ "가 없습니다.");
    }
}
