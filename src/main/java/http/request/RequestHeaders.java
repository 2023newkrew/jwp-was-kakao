package http.request;

import http.HttpRequestHeader;
import utils.RequestParsingUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RequestHeaders {
    private final Map<HttpRequestHeader, String> headers;

    public RequestHeaders(Map<HttpRequestHeader, String> headers) {
        this.headers = Map.copyOf(headers);
    }

    public static RequestHeaders fromRawHeaders(List<String> rawHeaders) {
        return RequestParsingUtils.parseRequestHeader(rawHeaders);
    }

    public Optional<String> get(HttpRequestHeader key) {
        return Optional.ofNullable(headers.getOrDefault(key, null));
    }
}
