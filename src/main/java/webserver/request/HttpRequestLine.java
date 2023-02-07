package webserver.request;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpMethod;

public class HttpRequestLine {

    private final HttpMethod httpMethod;
    private final String path;

    private final Map<String, String> queryParams;
    private final String httpVersion;

    public HttpRequestLine(HttpMethod httpMethod, String path, Map<String, String> queryParams, String httpVersion) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.queryParams = queryParams;
        this.httpVersion = httpVersion;
    }

    public HttpRequestLine(String stringStartLine) {
        if (stringStartLine == null) {
            throw new RuntimeException("null point error");
        }
        String[] tokens = stringStartLine.split(" ");
        httpMethod = HttpMethod.resolve(tokens[0]);
        if (httpMethod == null) {
            throw new RuntimeException("올바른 HTTP Method Type이 아닙니다.");
        }
        String requestUri = tokens[1].trim();
        path = requestUri.split("\\?")[0];
        queryParams = parseQueryParams(requestUri);
        httpVersion = tokens[2].trim();
    }

    public static Map<String, String> parseQueryParams(String requestTarget) {
        Map<String, String> queryParams = new HashMap<>();
        if (requestTarget.contains("?")) {
            String parameters = requestTarget.split("\\?")[1];
            Arrays.stream(parameters.split("&"))
                    .forEach((x) -> queryParams.put(x.split("=")[0], x.split("=")[1]));
        }
        return queryParams;
    }


    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}
