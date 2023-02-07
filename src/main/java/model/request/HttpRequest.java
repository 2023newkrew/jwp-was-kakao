package model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import model.enumeration.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private final Map<String, String> body;

    public String getParameter(String key) {
        return queryParams.get(key);
    }

    public static class HttpRequestBuilder {}


    public boolean isPOSTMethod() {
        return this.method.equals(HttpMethod.POST);
    }

    public String getContentType() {
        return this.headers.getOrDefault("Accept", "text/html,").split(",")[0];
    }

    public Map<String, String> getQueryParams() {
        return new HashMap<>(queryParams);
    }

    public Map<String, String> getBody() {
        return new HashMap<>(body);
    }

    public String getPath() {
        return path;
    }
}
