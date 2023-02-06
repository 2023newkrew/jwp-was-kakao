package http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUrl {

    private String path;
    private Map<String, String> params;

    public HttpUrl(String path) {
        this.path = path;
        this.params = parseParams(path);
    }

    public String getPath() {
        return path;
    }
    
    private Map<String, String> parseParams(String path) {
        if (!path.contains("?")) {
            return new HashMap<>();
        }

        String queryString = path.substring(path.indexOf("?") + 1);
        String[] parameters = queryString.split("&");

        return Arrays.stream(parameters)
                .map(parameter -> parameter.split("="))
                .collect(Collectors.toMap(parameter -> parameter[0], parameter -> parameter[1]));
    }
}
