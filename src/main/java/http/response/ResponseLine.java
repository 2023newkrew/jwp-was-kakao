package http.response;

import org.springframework.http.HttpStatus;

public class ResponseLine {
    private String protocol;
    private HttpStatus status;

    public ResponseLine() {
        this.protocol = "HTTP/1.1";
        this.status = HttpStatus.OK;
    }

    public ResponseLine(HttpStatus status) {
        this.status = status;
    }

    public ResponseLine(String protocol, HttpStatus status) {
        this.protocol = protocol;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s %s\r\n", protocol, status.toString());
    }
}
