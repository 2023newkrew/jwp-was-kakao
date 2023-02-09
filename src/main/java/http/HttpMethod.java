package http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {
    GET, HEAD, POST, PUT, DELETE, CONNECT, OPTION, TRACE, PATCH;

    private static final Map<String, HttpMethod> mapping = new HashMap<>();

    static {
        Arrays.stream(HttpMethod.values())
                .forEach(httpMethod -> mapping.put(httpMethod.name(), httpMethod));
    }

    public static HttpMethod of(String method) {
        return mapping.get(method);
    }
}