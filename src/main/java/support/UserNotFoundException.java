package support;

import common.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당하는 사용자가 존재하지 않습니다.");
    }
}
