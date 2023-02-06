package webserver.controller;

import org.springframework.http.HttpMethod;

import java.util.Objects;

public class PathMapKey {
    private HttpMethod httpMethod;
    private String path;

    public PathMapKey(HttpMethod httpMethod, String path) {
        this.path = path;
        this.httpMethod = httpMethod;
    }

    public static PathMapKey of(HttpMethod httpMethod, String path) {
        return new PathMapKey(httpMethod, path);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        PathMapKey mappingPath = (PathMapKey) obj;
        return mappingPath.httpMethod.equals(httpMethod) && Objects.equals(mappingPath.path, path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, path);
    }
}
