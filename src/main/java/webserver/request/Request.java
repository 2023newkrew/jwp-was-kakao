package webserver.request;

import org.springframework.http.HttpMethod;
import webserver.request.path.URL;

public class Request {

    private final HttpMethod httpMethod;

    private final URL URL;

    public Request(HttpMethod httpMethod, URL URL) {
        this.httpMethod = httpMethod;
        this.URL = URL;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return URL.getPath();
    }

    public String getPathVariable(String key) {
        return URL.getPathVariable(key);
    }
}
