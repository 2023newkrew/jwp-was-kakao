package webserver;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestLine {
    private static final String START_LINE_DELIMITER = " ";
    private static final String QUERY_STRING_DELIMITER = "&";
    private static final String KEY_VALUE_ASSIGNMENT = "=";
    private static final String QUERY_STRING_PREFIX_REGEX = "\\?";
    private static final String QUERY_STRING_PREFIX = "?";

    private final String method;
    private final String path;
    private final Map<String, String> querystring;

    public HttpRequestLine(String method, String path, Map<String, String> querystring) {
        this.method = method;
        this.path = path;
        this.querystring = querystring;
    }

    public static HttpRequestLine from(String startLine) {
        String method = startLine.split(START_LINE_DELIMITER)[0];
        String uri = startLine.split(START_LINE_DELIMITER)[1];

        if (hasQueryString(uri)) {
            String path = uri.split(QUERY_STRING_PREFIX_REGEX)[0];
            String queryString = uri.split(QUERY_STRING_PREFIX_REGEX)[1];
            Map<String, String> parameters = parseToParameters(queryString);
            return new HttpRequestLine(method, path, parameters);
        }

        return new HttpRequestLine(method, uri, Map.of());
    }

    private static Map<String, String> parseToParameters(String queryString) {
        Map<String, String> parameters = new HashMap<>();

        for (String parameter : queryString.split(QUERY_STRING_DELIMITER)) {
            String key = parameter.split(KEY_VALUE_ASSIGNMENT)[0];
            String value = URLDecoder.decode(parameter.split(KEY_VALUE_ASSIGNMENT)[1], Charset.defaultCharset());
            parameters.put(key, value);
        }

        return parameters;
    }

    private static boolean hasQueryString(String uri) {
        return uri.contains(QUERY_STRING_PREFIX);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getParameter(String key) {
        return querystring.get(key);
    }
}
