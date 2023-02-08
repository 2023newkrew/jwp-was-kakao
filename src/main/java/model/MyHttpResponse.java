package model;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class MyHttpResponse {

    private static final String EOL = "\r\n";
    private static final String PROTOCOL = "HTTP/1.1";
    private int status = 200;
    private String statusMessage = "OK";
    private final Map<String, String> headers = new HashMap<>();

    public void setLocation(String location) {
        this.headers.put("Location", location);
    }

    public void setStatus(HttpStatus httpStatus) {
        this.status = httpStatus.value();
        this.statusMessage = httpStatus.getReasonPhrase();
    }

    public void setContentType(String contentType) {
        this.headers.put("Content-Type", contentType + "; charset=utf-8");
    }

    public boolean isRedirectRequired() {
        return this.status >= 300 && this.status < 400;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %d %s", PROTOCOL, status, statusMessage)).append(EOL);
        headers.forEach(
                (key, value) -> sb.append(key).append(": ").append(value).append(EOL));
        return sb.toString();
    }
}
