package exception;

import static constant.ErrorMessageConstant.NO_SUCH_METHOD;

public class NoSuchMethodException extends RuntimeException {
    public NoSuchMethodException() {
        super(NO_SUCH_METHOD);
    }

    public NoSuchMethodException(Throwable cause) {
        super(NO_SUCH_METHOD, cause);
    }
}
