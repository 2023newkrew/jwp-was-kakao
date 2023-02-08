package infra.utils.validator;

public class Validator {
    public static <T> void checkField(T field, String fieldName) {
        FieldValidator.checkField(field, fieldName);
    }
}
