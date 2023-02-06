package http.request;

import utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpUrl {

    private String path;
    private Map<String, String> params;

    public HttpUrl(String path) {
        this.path = parsePath(path);
        this.params = parseParams(path);
    }

    public String parsePath(String path) {
        if (!path.contains("?")) {
            return path;
        }

        return path.substring(0, path.indexOf("?"));
    }

    private Map<String, String> parseParams(String path) {
        if (!path.contains("?")) {
            return new HashMap<>();
        }

        String queryString = path.substring(path.indexOf("?") + 1);
        return ParameterUtils.parse(queryString);
    }

    public String getPath() {
        return path;
    }
}
