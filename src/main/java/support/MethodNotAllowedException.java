package support;

import common.HttpStatus;

public class MethodNotAllowedException extends CustomException {
    public MethodNotAllowedException() {
        super(HttpStatus.METHOD_NOT_ALLOWED, "해당하는 경로에서 이 메서드는 허용되지 않습니다");
    }
}
