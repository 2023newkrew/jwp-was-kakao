package model.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QueryString {
    private Map<String, String> queryString = new HashMap<>();

    public Map<String, String> getQueryString() {
        return Collections.unmodifiableMap(queryString);
    }

    private QueryString(Map queryString) {
        this.queryString = queryString;
    }

    public static QueryString of(Map queryString) {
        return new QueryString(queryString);
    }
}
