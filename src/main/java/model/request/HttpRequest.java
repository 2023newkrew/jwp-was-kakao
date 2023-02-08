package model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import model.annotation.Api;
import model.enumeration.ContentType;
import model.enumeration.HttpMethod;
import model.request.properties.HttpRequestFirstLineProperties;
import model.request.properties.QueryParams;
import model.request.properties.RequestBody;
import model.request.properties.RequestHeaders;
import model.web.Session;
import model.web.SessionManager;

import static constant.DefaultConstant.*;
import static constant.HeaderConstant.*;

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

    public boolean methodAndURLAndContentEquals(Api annotation) {
        return method.equals(annotation.method()) &&
                URL.equals(annotation.url()) &&
                isContentTypeCanConsume(annotation);

    }

    private boolean isContentTypeCanConsume(Api annotation) {
        return findContentType().equals(annotation.consumes()) ||
                annotation.consumes().equals(ContentType.ANY);
    }

    private ContentType findContentType() {
        return ContentType.of(findHeaderValue(CONTENT_TYPE, DEFAULT_CONTENT_TYPE));
    }

    public HttpRequest(HttpRequestFirstLineProperties firstLineProperties,
                       RequestHeaders requestHeaders,
                       RequestBody requestBody) {
        this.method = firstLineProperties.getHttpMethod();
        this.protocol = firstLineProperties.getHttpProtocol();
        this.URL = firstLineProperties.getURL();
        this.queryParams = firstLineProperties.getQueryParams();
        this.headers = requestHeaders;
        this.body = requestBody;
    }

    public Session getSession(String id) {
        return SessionManager.findSession(id);
    }
}
