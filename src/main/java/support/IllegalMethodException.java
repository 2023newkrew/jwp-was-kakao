package support;

import common.HttpStatus;

public class IllegalMethodException extends CustomException {

    public IllegalMethodException() {
        super(HttpStatus.BAD_REQUEST, "요청에 해당하는 HTTP Method가 존재하지 않습니다.");
    }
}
