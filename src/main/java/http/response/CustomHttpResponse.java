package http.response;

import java.util.Map;


public class CustomHttpResponse {

    private final String protocol;
    private final CustomHttpStatus httpStatus;
    private final Map<String, String> headers;
    private final String body;

    public CustomHttpResponse(String protocol, CustomHttpStatus httpStatus, Map<String, String> headers, String body) {
        this.protocol = protocol;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public String getProtocol() {
        return protocol;
    }

    public CustomHttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
