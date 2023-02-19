package model.request.properties;

import java.util.Collections;
import java.util.Map;

public class QueryParams {
    private final Map<String, String> queryParams;

    private QueryParams(Map<String, String> queryString) {
        this.queryParams = queryString;
    }

    public static QueryParams of(Map<String, String> queryString) {
        return new QueryParams(queryString);
    }

    public Map<String, String> getQueryParams() {
        return Collections.unmodifiableMap(queryParams);
    }
}
