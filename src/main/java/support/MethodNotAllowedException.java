package support;

public class MethodNotAllowedException extends RuntimeException {
    public MethodNotAllowedException() {
        super("해당하는 경로에서 이 메서드는 허용되지 않습니다");
    }
}
