package webserver;

import java.util.Map;
import java.util.Optional;

public class HttpRequestHeader {
    public static final String CONTENT_LENGTH = "Content-Length";
    private final Map<String, String> headers;

    public HttpRequestHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public Optional<Integer> getContentLength() {
        return Optional.ofNullable(headers.get(CONTENT_LENGTH))
                .map(Integer::parseInt);
    }
}
