package infra.utils;

import java.util.Objects;
import org.springframework.http.HttpMethod;

public class Api {

    private final String path;
    private final HttpMethod method;

    public Api(String path, HttpMethod method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Api api = (Api) o;
        return Objects.equals(path, api.path) && method == api.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }

    @Override
    public String toString() {
        return "Api{" +
                "path='" + path + '\'' +
                ", method=" + method +
                '}';
    }
}
