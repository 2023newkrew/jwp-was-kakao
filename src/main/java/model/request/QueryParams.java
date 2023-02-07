package model.request;

import java.util.Collections;
import java.util.Map;

public class QueryParams {
    private final Map<String, String> queryParams;

    public Map<String, String> getQueryParams() {
        return Collections.unmodifiableMap(queryParams);
    }

    private QueryParams(Map<String, String> queryString) {
        this.queryParams = queryString;
    }

    public static QueryParams of(Map<String, String> queryString) {
        return new QueryParams(queryString);
    }
}
