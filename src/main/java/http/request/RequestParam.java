package http.request;

import java.util.HashMap;
import java.util.Map;

public class RequestParam {
    private static final String DELIMITER = "&";
    private static final String KEY_VALUE = "=";

    private final Map<String, String> params = new HashMap<>();

    public RequestParam() {}

    public RequestParam(String query) {
        if (query == null || query.isEmpty()) {
            return;
        }
        for (String param : query.split(DELIMITER)) {
            String[] keyValue = param.split(KEY_VALUE, 2);
            params.put(keyValue[0], keyValue[1]);
        }
    }

    public String get(String key) {
        String value = params.get(key);
        if (value == null) {
            throw new IllegalArgumentException();
        }
        return value;
    }
}
