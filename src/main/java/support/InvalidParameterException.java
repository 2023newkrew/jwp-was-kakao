package support;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String key) {
        super("파라미터 " + key + "가 유효하지 않은 값입니다.");
    }
}
