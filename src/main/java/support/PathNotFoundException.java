package support;

import common.HttpStatus;

public class PathNotFoundException extends CustomException {
    public PathNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당하는 경로가 존재하지 않습니다");
    }
}
