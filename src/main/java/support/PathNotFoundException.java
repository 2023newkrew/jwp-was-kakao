package support;

public class PathNotFoundException extends RuntimeException {
    public PathNotFoundException() {
        super("해당하는 경로가 존재하지 않습니다");
    }
}
