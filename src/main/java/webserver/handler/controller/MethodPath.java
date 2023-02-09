package webserver.handler.controller;

import org.springframework.http.HttpMethod;
import webserver.request.Request;

public class MethodPath {

    private final HttpMethod httpMethod;

    private final String path;

    public MethodPath(Request request) {
        this(request.getHttpMethod(), request.getPath());
    }

    public MethodPath(HttpMethod httpMethod, String path) {
        this.httpMethod = httpMethod;
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodPath)) {
            return false;
        }

        MethodPath that = (MethodPath) o;

        if (httpMethod != that.httpMethod) {
            return false;
        }
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        int result = httpMethod.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}
