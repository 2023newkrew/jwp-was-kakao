package exception;

import static constant.ErrorMessageConstant.NO_SUCH_COOKIE;

public class NoSuchCookieException extends RuntimeException {
    public NoSuchCookieException() {
        super(NO_SUCH_COOKIE);
    }

    public NoSuchCookieException(Throwable cause) {
        super(NO_SUCH_COOKIE, cause);
    }
}
