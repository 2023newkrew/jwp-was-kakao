package support;

import common.HttpStatus;

public class UnsupportedContentTypeException extends CustomException {
    public UnsupportedContentTypeException() {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원되지 않는 형식의 내용입니다.");
    }
}
