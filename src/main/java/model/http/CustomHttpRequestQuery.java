package model.http;

import java.util.Map;

public class CustomHttpRequestQuery extends CustomBaseHttpRequest {

    private Map<String, String> requestQuery;

    public CustomHttpRequestQuery(Map<String, String> requestQuery) {
        this.requestQuery = requestQuery;
    }

    @Override
    public String get(String key) {
        return requestQuery.getOrDefault(key, null);
    }

    @Override
    public void put(String key, String value) {
        requestQuery.put(key, value);
    }

}
