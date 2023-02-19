package exception;

import static constant.ErrorMessageConstant.TEMPLATE_CANNOT_LOADED;

public class TemplateCannotLoadedException extends RuntimeException {
    public TemplateCannotLoadedException() {
        super(TEMPLATE_CANNOT_LOADED);
    }

    public TemplateCannotLoadedException(Throwable cause) {
        super(TEMPLATE_CANNOT_LOADED, cause);
    }
}
