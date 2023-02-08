package support;

public class IllegalMethodException extends RuntimeException {
    public IllegalMethodException() {
        super("요청에 해당하는 HTTP Method가 존재하지 않습니다.");
    }
}
