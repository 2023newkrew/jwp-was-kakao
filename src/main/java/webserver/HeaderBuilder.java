package webserver;

import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class HeaderBuilder {
    private final HttpStatus httpStatus;
    private final Map<String, String > preHeaders;

    public HeaderBuilder(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.preHeaders = new LinkedHashMap<>();
    }

    public HeaderBuilder addHeader(String key, String value) {
        preHeaders.put(key, value);
        return this;
    }

    public HeaderBuilder addContentType(String contentType) {
        return addHeader("Content-Type", contentType + ";charset=utf-8");
    }

    public HeaderBuilder addContentLength(int contentLength) {
        return addHeader("Content-Length", String.valueOf(contentLength));
    }

    public HeaderBuilder addLocation(String location) {
        return addHeader("Location", location);
    }

    public String buildToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(httpStatus.toString()).append(" \r\n");
        preHeaders.forEach((key, value) -> {
            sb.append(key).append(": ").append(value).append(" \r\n");
        });
        sb.append("\r\n");
        return sb.toString();
    }
}
