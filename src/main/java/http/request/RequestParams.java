package http.request;

import utils.ParsingUtils;

import java.util.Map;
import java.util.Optional;

public final class RequestParams extends RequestData {
    private final Map<String, String> params;

    public RequestParams(Map<String, String> params) {
        this.params = Map.copyOf(params);
    }

    public static RequestParams fromQueryString(String queryString) {
        return new RequestParams(
                ParsingUtils.parseQueryString(queryString)
        );
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(params.getOrDefault(key, null));
    }
}
