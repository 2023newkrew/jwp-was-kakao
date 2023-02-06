package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class QueryParamters {

    private final Map<String, String> parameters = new HashMap<>();

    public QueryParamters(String url) {
        String[] pathToken = url.split("\\?");
        if (pathToken.length > 1) {
            setQueryParameters(pathToken[1]);
        };
    }

    private void setQueryParameters(String queryString) {
        String[] tokens = queryString.split("&");
        for (String token : tokens) {
            String[] keyValue = token.split("=");
            if (keyValue.length != 2) {
                throw new RuntimeException("InvalidQueryStringException");
            }
            parameters.put(keyValue[0], keyValue[1]);
        }
    }

    public String getValue(String key) {
        return parameters.get(key);
    }
}
