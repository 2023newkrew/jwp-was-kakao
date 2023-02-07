package webserver.request;

import org.springframework.http.HttpMethod;
import webserver.request.path.PathVariables;
import webserver.request.path.URL;

public class Request {

    private final HttpMethod httpMethod;

    private final URL URL;

    private final String body;

    public Request(HttpMethod httpMethod, URL URL, String body) {
        this.httpMethod = httpMethod;
        this.URL = URL;
        this.body = body;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return URL.getPath();
    }

    public PathVariables getBodyAsPathVariables() {
        return new PathVariables(body);
    }
}
