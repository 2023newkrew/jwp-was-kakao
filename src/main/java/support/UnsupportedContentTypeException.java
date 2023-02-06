package support;

public class UnsupportedContentTypeException extends RuntimeException {
    public UnsupportedContentTypeException() {
        super("지원되지 않는 형식의 내용입니다.");
    }
}
