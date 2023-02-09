package exception;

import static constant.ErrorMessageConstant.UNSUPPORTED_CONTENT_TYPE;

public class UnsupportedContentTypeException extends RuntimeException {
    public UnsupportedContentTypeException() {
        super(UNSUPPORTED_CONTENT_TYPE);
    }

    public UnsupportedContentTypeException(Throwable cause) {
        super(UNSUPPORTED_CONTENT_TYPE, cause);
    }
}
