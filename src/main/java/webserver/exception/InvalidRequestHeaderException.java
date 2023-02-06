package webserver.exception;

public class InvalidRequestHeaderException extends RuntimeException {
    public InvalidRequestHeaderException() {
        super("잘못된 요청 헤더");
    }
}
