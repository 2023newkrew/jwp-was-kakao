package model;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

@Getter
@ToString
public class HttpRequest {

    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private final Map<String, String> body;

    public HttpRequest(String httpMethod, String url, Map<String, String> queryParams, Map<String, String> headers,
            Map<String, String> body) {
        this(HttpMethod.resolve(httpMethod), url, queryParams, headers, body);
    }

    public HttpRequest(HttpMethod httpMethod, String url, Map<String, String> queryParams, Map<String, String> headers,
            Map<String, String> body) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.queryParams = new HashMap<>(queryParams);
        this.headers = new HashMap<>(headers);
        this.body = new HashMap<>(body);
    }

    public boolean isPOSTMethod() {
        return this.httpMethod.equals(HttpMethod.POST);
    }

    public String getContentType() {
        return this.headers.getOrDefault("Accept", "text/html,").split(",")[0];
    }

    public Map<String, String> getQueryParams() {
        return new HashMap<>(queryParams);
    }
}
