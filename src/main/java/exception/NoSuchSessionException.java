package exception;

import static constant.ErrorMessageConstant.NO_SUCH_SESSION;

public class NoSuchSessionException extends RuntimeException {
    public NoSuchSessionException() {
        super(NO_SUCH_SESSION);
    }

    public NoSuchSessionException(Throwable cause) {
        super(NO_SUCH_SESSION, cause);
    }
}
