package exception;

import static constant.ErrorMessageConstant.DATA_CANNOT_LOADED;

public class DataCannotLoadedException extends RuntimeException {
    public DataCannotLoadedException() {
        super(DATA_CANNOT_LOADED);
    }

    public DataCannotLoadedException(Throwable cause) {
        super(DATA_CANNOT_LOADED, cause);
    }
}
