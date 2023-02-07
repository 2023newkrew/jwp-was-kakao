package model.request;

import java.util.Collections;
import java.util.Map;

public class QueryParams {
    private final Map queryParams;

    public Map getQueryParams() {
        return Collections.unmodifiableMap(queryParams);
    }

    private QueryParams(Map queryString) {
        this.queryParams = queryString;
    }

    public static QueryParams of(Map queryString) {
        return new QueryParams(queryString);
    }
}
