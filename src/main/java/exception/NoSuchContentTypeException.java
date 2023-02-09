package exception;

import static constant.ErrorMessageConstant.NO_SUCH_CONTENT_TYPE;

public class NoSuchContentTypeException extends RuntimeException {
    public NoSuchContentTypeException() {
        super(NO_SUCH_CONTENT_TYPE);
    }

    public NoSuchContentTypeException(Throwable cause) {
        super(NO_SUCH_CONTENT_TYPE, cause);
    }
}
