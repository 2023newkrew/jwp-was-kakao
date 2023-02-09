package webserver.request;

import org.springframework.http.HttpMethod;
import webserver.collection.StringValues;
import webserver.collection.Values;

public class RequestHead {

    public static final String DELIMITER = " ";

    public static final String PATH_DELIMITER = "\\?";

    public static final String VARIABLE_DELIMITER = "&";

    public static final String KEY_VALUE_DELIMITER = "=";

    private final HttpMethod httpMethod;

    private final String path;

    private final Values pathVariables;

    public RequestHead(String headLine) {
        String[] headLines = headLine.split(DELIMITER);
        String httpMethod = headLines[0];
        String url = headLines[1];
        String[] urlSplit = url.split(PATH_DELIMITER);

        this.httpMethod = HttpMethod.resolve(httpMethod);
        this.path = urlSplit[0];
        this.pathVariables = getPathVariables(urlSplit);
    }

    private Values getPathVariables(String[] urlSplit) {
        if (urlSplit.length < 2) {
            return null;
        }

        return new StringValues(urlSplit[1], VARIABLE_DELIMITER, KEY_VALUE_DELIMITER);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getPathVariable(String key) {
        return pathVariables.get(key);
    }
}
