package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

@Getter
@ToString
public class MyHttpRequest {

    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private final Map<String, String> body;
    private final Map<String, String> cookies;
    private final List<String> staticUrls = Arrays.asList("css", "js", "images", "fonts", "favicon");

    public MyHttpRequest(String httpMethod, String url, Map<String, String> queryParams, Map<String, String> headers,
            Map<String, String> cookies, Map<String, String> body) {
        this(HttpMethod.resolve(httpMethod), url, queryParams, headers, cookies, body);
    }

    public MyHttpRequest(HttpMethod httpMethod, String url, Map<String, String> queryParams,
            Map<String, String> headers, Map<String, String> cookies, Map<String, String> body) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.queryParams = new HashMap<>(queryParams);
        this.headers = new HashMap<>(headers);
        this.cookies = new HashMap<>(cookies);
        this.body = new HashMap<>(body);
    }

    public boolean isStaticRequest() {
        return staticUrls.stream()
                .anyMatch(staticUrl -> this.url.startsWith(staticUrl, 1));
    }

    public String getContentType() {
        return this.headers.getOrDefault("Accept", "text/html,").split(",")[0];
    }

    public Map<String, String> getBody() {
        return new HashMap<>(body);
    }
}
