package webserver.router;

import http.request.HttpMethod;

import java.util.Objects;

public class Route {
    private final HttpMethod method;
    private final String path;

    public Route(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public static Route of(HttpMethod method, String path) {
        return new Route(method, path);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return getMethod() == route.getMethod() && getPath().equals(route.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMethod(), getPath());
    }
}
