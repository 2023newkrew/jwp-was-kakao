package model.http;

import java.util.Map;

public class CustomHttpRequestBody extends CustomBaseHttpRequest {

    private Map<String, String> requestBody;

    public CustomHttpRequestBody(Map<String, String> requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public String get(String key) {
        return requestBody.getOrDefault(key, null);
    }

    @Override
    public void put(String key, String value) {
        requestBody.put(key, value);
    }

}
