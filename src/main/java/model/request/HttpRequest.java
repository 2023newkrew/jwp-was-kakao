package model.request;

import model.annotation.Api;
import model.enumeration.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import model.request.properties.HttpRequestFirstLineProperties;
import model.request.properties.QueryParams;
import model.request.properties.RequestBody;
import model.request.properties.RequestHeaders;

import static constant.DefaultConstant.*;

@Getter
@AllArgsConstructor
@Builder
public class HttpRequest {
    private HttpMethod method;
    private String protocol;
    private String URL;
    private QueryParams queryParams;
    private RequestHeaders headers;
    private RequestBody body;

    public String findBodyValue(String key, String defaultValue) {
        return body.getRequestBody().getOrDefault(key, defaultValue);
    }

    public String findHeaderValue(String key, String defaultValue) {
        return headers.getHeaders().getOrDefault(key, defaultValue);
    }
    public boolean methodAndURLEquals(Api annotation) {
        return method.equals(annotation.method()) && URL.equals(annotation.url());
    }

    public boolean isNotDefaultURL() {
        return !URL.equals(DEFAULT_URL);
    }

    public HttpRequest (HttpRequestFirstLineProperties firstLineProperties,
                        RequestHeaders requestHeaders,
                        RequestBody requestBody) {
        this.method = firstLineProperties.getHttpMethod();
        this.protocol = firstLineProperties.getHttpProtocol();
        this.URL = firstLineProperties.getURL();
        this.queryParams = firstLineProperties.getQueryParams();
        this.headers = requestHeaders;
        this.body = requestBody;
    }
}
