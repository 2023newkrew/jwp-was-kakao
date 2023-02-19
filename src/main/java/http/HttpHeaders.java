package http;

import java.util.*;

public class HttpHeaders {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String LOCATION = "Location";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String COOKIE = "Cookie";

    private final Map<String, List<String>> headers = new LinkedHashMap<>();

    public HttpHeaders() {
    }

    public HttpHeaders(Map<String, List<String>> headers) {
        this.headers.putAll(headers);
    }

    public List<String> getHeader(String name) {
        return headers.get(name);
    }

    public void setHeader(String name, List<String> value) {
        headers.put(name, value);
    }

    public Map<String, List<String>> getHeaders() {
        return new LinkedHashMap<>(headers);
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers.putAll(headers);
    }

    public boolean removeHeader(String name) {
        String headerName = headers.keySet().stream()
                .filter(header -> header.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (headerName == null) {
            return false;
        }

        headers.remove(headerName);
        return true;
    }

    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public boolean isEmpty() {
        return headers.isEmpty();
    }

    public boolean hasHeader(String name) {
        return headers.keySet().stream()
                .anyMatch(header -> header.equalsIgnoreCase(name));
    }

    public int size() {
        return headers.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpHeaders that = (HttpHeaders) o;
        return Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }

    @Override
    public String toString() {
        return "HttpHeaders{" +
                "headers=" + headers +
                '}';
    }
}
