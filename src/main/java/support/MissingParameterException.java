package support;

public class MissingParameterException extends RuntimeException {
    public MissingParameterException(String key) {
        super("요구되는 파라미터 " + key+ "가 없습니다.");
    }
}
