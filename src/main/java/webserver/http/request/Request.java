package webserver.http.request;

import org.springframework.http.HttpMethod;
import webserver.http.request.path.PathVariables;
import webserver.http.request.path.URL;

public class Request {

    private final HttpMethod httpMethod;

    private final URL url;

    private final String body;

    public Request(HttpMethod httpMethod, URL url, String body) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.body = body;
    }

    public MethodPath getMethodPath() {
        return new MethodPath(httpMethod, getPath());
    }

    public String getPath() {
        return url.getPath();
    }

    public PathVariables getBodyAsPathVariables() {
        return new PathVariables(body);
    }
}
