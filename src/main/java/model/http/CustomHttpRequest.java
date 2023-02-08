package model.http;

public class CustomHttpRequest {

    private final CustomHttpMethod customHttpMethod;
    private final String url;
    private final CustomHttpRequestQuery query;
    private final String protocol;
    private final CustomHttpHeader headers;
    private final CustomHttpRequestBody body;

    public CustomHttpRequest(
            CustomHttpMethod customHttpMethod,
            String url,
            CustomHttpRequestQuery query,
            String protocol,
            CustomHttpHeader headers,
            CustomHttpRequestBody body) {
        this.customHttpMethod = customHttpMethod;
        this.url = url;
        this.query = query;
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    public CustomHttpMethod getHttpMethod() {
        return customHttpMethod;
    }

    public String getUrl() {
        return url;
    }

    public CustomHttpRequestQuery getQuery() {
        return query;
    }

    public String getProtocol() {
        return protocol;
    }

    public CustomHttpHeader getHeaders() {
        return headers;
    }

    public CustomHttpRequestBody getBody() {
        return body;
    }
}
