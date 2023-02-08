package infra.utils.validator;

import infra.exception.NullOrBlankFieldException;
import java.util.Objects;
import org.springframework.util.StringUtils;

public class FieldValidator {

    private FieldValidator() {
    }

    public static <T> void checkField(T field, String fieldName) {
        if (field instanceof String) {
            checkStringField((String) field, fieldName);
            return;
        }
        checkOtherField(field, fieldName);
    }

    private static <T> void checkOtherField(T field, String fieldName) {
        if (Objects.isNull(field)) {
            throw new NullOrBlankFieldException("null", fieldName);
        }
    }

    private static void checkStringField(String field, String fieldName) {
        if (StringUtils.isEmpty(field)) {
            throw new NullOrBlankFieldException("null or blank", fieldName);
        }
    }
}
