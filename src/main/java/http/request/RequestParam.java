package http.request;

import java.util.HashMap;
import java.util.Map;

public class RequestParam {
    private static final String DELIMITER = "&";
    private static final String KEY_VALUE = "=";
    private static final RequestParam EMPTY_REQUEST_PARAM = new RequestParam("");

    private final Map<String, String> params = new HashMap<>();

    public RequestParam(String query) {
        if (query == null || query.isEmpty()) {
            return;
        }
        for (String param : query.split(DELIMITER)) {
            String[] keyValuePair = param.split(KEY_VALUE, 2);
            params.put(keyValuePair[0], keyValuePair[1]);
        }
    }

    public static RequestParam empty() { return EMPTY_REQUEST_PARAM; }

    public String get(String key) {
        String value = params.get(key);
        if (value == null) {
            throw new IllegalArgumentException();
        }
        return value;
    }
}
