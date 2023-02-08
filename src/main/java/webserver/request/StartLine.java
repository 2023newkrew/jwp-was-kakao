package webserver.request;

import java.util.Map;
import lombok.Builder;
import webserver.ResourceType;

@Builder
public class StartLine {

    private HttpMethod httpMethod;

    private String url;

    private Map<String, String> queryParams;

    private String httpVersion;


    public StartLine(HttpMethod httpMethod, String url, Map<String, String> queryParams, String httpVersion) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.queryParams = queryParams;
        this.httpVersion = httpVersion;

    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String convertToAbsolutePath(ResourceType resourceType) {
        return resourceType.getPath() + url;
    }

}
