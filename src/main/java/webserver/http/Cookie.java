package webserver.http;

import webserver.collection.StringValues;
import webserver.collection.Values;

public class Cookie {

    private static final String DELIMITER = ";";

    private static final String KEY_VALUE_DELIMITER = "=";

    private final String value;

    private final String path;

    public Cookie(String jSessionId, String path) {
        this.value = jSessionId;
        this.path = path;
    }

    public Cookie(String cookie) {
        Values values = new StringValues(cookie, DELIMITER, KEY_VALUE_DELIMITER);
        this.value = values.get("JSESSIONID");
        this.path = values.get("Path");
    }

    public String getJSessionId() {
        return value;
    }

    @Override
    public String toString() {
        return "JSESSIONID=" + value + "; Path=" + path;
    }
}
