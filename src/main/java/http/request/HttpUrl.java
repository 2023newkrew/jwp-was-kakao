package http.request;

import utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpUrl {

    private static final String QUERY_STRING_DELIMITER = "?";

    private final String path;
    private final Map<String, String> params;

    public HttpUrl(String path) {
        this.path = parsePath(path);
        this.params = parseParams(path);
    }

    public String parsePath(String path) {
        if (!path.contains(QUERY_STRING_DELIMITER)) {
            return path;
        }

        return path.substring(0, path.indexOf(QUERY_STRING_DELIMITER));
    }

    private Map<String, String> parseParams(String path) {
        if (!path.contains(QUERY_STRING_DELIMITER)) {
            return new HashMap<>();
        }

        String queryString = path.substring(path.indexOf(QUERY_STRING_DELIMITER) + 1);
        return ParameterUtils.parse(queryString);
    }

    public String getPath() {
        return path;
    }
}
