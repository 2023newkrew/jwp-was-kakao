package webserver.response;

import java.util.Map;

public class Response {

    private final StatusCode statusCode;
    private final String body;
    private final Map<String, String> headers;
    private final String version;

    public Response(StatusCode statusCode, String body, Map<String, String> headers, String version) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
        this.version = version;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getVersion() {
        return version;
    }

}
