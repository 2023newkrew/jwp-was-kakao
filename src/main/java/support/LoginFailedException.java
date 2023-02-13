package support;

import common.HttpStatus;

public class LoginFailedException extends CustomException {
    public LoginFailedException() {
        super(HttpStatus.BAD_REQUEST, "로그인이 실패했습니다.");
    }
}
