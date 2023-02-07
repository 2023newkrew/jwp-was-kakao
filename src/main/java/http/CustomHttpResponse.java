package http;

import java.util.Map;


public class CustomHttpResponse {

    private final String httpStatus;
    private final Map<String, String> headers;
    private final String body;

    public String getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public CustomHttpResponse(String httpStatus, Map<String, String> headers, String body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }
}
