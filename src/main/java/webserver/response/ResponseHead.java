package webserver.response;

import org.springframework.http.HttpStatus;

public class ResponseHead {

    private final String head;

    public ResponseHead(HttpStatus httpStatus) {
        this.head = "HTTP/1.1 " + httpStatus + "\r\n";
    }

    @Override
    public String toString() {
        return head;
    }
}
