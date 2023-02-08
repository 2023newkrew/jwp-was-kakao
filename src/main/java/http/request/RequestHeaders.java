package http.request;

import java.util.Map;
import java.util.Optional;

public class RequestHeaders {
    private final Map<String, String> headers;
    public RequestHeaders(Map<String, String> headers) {
        this.headers = Map.copyOf(headers);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(headers.getOrDefault(key, null));
    }
}
