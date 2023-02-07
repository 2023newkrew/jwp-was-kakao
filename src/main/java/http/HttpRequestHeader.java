package http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class HttpRequestHeader {
    private final Map<HttpHeaders, String> headers = new HashMap<>();

    public HttpRequestHeader() { }

    public Optional<String> getAttribute(HttpHeaders key) {
        return Optional.ofNullable(headers.get(key));
    }

    public void addAttribute(HttpHeaders header, String value) {
        headers.put(header, value);
    }
}
