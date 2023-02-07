package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class QueryParams {

    Map<String, String> params;

    private static final String QUERY_SPLIT_DELIMITER = "&";

    private static final String PARAM_SPLIT_DELIMITER = "=";

    public QueryParams(String query) {
        parseQueryParams(query);
    }

    private void parseQueryParams(String query) {
        this.params = new HashMap<>();
        String[] elements = query.split(QUERY_SPLIT_DELIMITER);
        for (String param : elements) {
            String[] kv = param.split(PARAM_SPLIT_DELIMITER);
            params.put(kv[0], kv[1]);
        }
    }

    public String get(String key) {
        return params.get(key);
    }
}
