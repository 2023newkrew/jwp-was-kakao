package webserver.http;

import webserver.collection.StringValues;
import webserver.collection.Values;

import java.util.stream.Collectors;

public class Cookie {

    private static final String DELIMITER = ";";

    private static final String KEY_VALUE_DELIMITER = "=";

    private final Values values;

    public Cookie(String jSessionId, String path) {
        values = new StringValues();
        putJSessionId(jSessionId);
        putPath(path);
    }

    public Cookie(String cookie) {
        values = new StringValues(cookie, DELIMITER, "=");
    }

    public void putJSessionId(String jSessionId) {
        values.put("JSESSIONID", jSessionId);
    }

    public void putPath(String path) {
        values.put("Path", path);
    }

    @Override
    public String toString() {
        return values.keySet()
                .stream()
                .map(key -> key + KEY_VALUE_DELIMITER + values.get(key))
                .collect(Collectors.joining(DELIMITER + " "));
    }
}
