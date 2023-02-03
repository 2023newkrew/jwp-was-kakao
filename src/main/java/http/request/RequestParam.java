package http.request;

import java.util.HashMap;
import java.util.Map;

public class RequestParam {
    private static final String DELIMITER = "&";
    private static final String KEY_VALUE = "=";

    private final Map<String, String> params = new HashMap<>();

    public RequestParam() {}

    public RequestParam(String query) {
        for (String param : query.split(DELIMITER)) {
            String[] keyValue = param.split(KEY_VALUE, 2);
            params.put(keyValue[0], keyValue[1]);
        }
    }
}
