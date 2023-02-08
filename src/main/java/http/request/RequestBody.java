package http.request;

import utils.ParsingUtils;

import java.util.Map;
import java.util.Optional;

public final class RequestBody extends RequestData {
    private final Map<String, String> body;

    public RequestBody(Map<String, String> body) {
        this.body = Map.copyOf(body);
    }

    public static RequestBody fromQueryString(String queryString) {
        return new RequestBody(
                ParsingUtils.parseQueryString(queryString)
        );
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(body.getOrDefault(key, null));
    }
}
