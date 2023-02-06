package webserver.exception;

public class InvalidQueryStringException extends RuntimeException {
    public InvalidQueryStringException() {
        super("잘못된 쿼리 스트링");
    }
}
