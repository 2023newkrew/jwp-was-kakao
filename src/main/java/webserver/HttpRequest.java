package webserver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class HttpRequest {
    private String httpMethod;
    private String url;
    private Map<String, String> queryString;
    private String protocol;
    private Map<String, String> headers;
    private Map<String, String> body;

    public boolean isHttpMethodEquals(String httpMethod) {
        return this.httpMethod.equals(httpMethod);
    }

    public boolean isURLEquals(String url) {
        return this.url.equals(url);
    }
}
