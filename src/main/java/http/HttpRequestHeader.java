package http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

class HttpRequestHeader {
    private final Map<HttpHeaders, String> headers = new HashMap<>();

    protected HttpRequestHeader() { }

    public Optional<String> getAttribute(HttpHeaders key) {
        return Optional.ofNullable(headers.get(key));
    }

    public void addAttribute(HttpHeaders header, String value) {
        headers.put(header, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestHeader that = (HttpRequestHeader) o;
        return Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }

    @Override
    public String toString() {
        return "HttpRequestHeader{" +
                "headers=" + headers +
                '}';
    }
}
