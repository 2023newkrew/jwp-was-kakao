package webserver.request;

import webserver.collection.StringValues;
import webserver.collection.Values;

import java.util.Objects;

public class RequestBody {

    private static final String BODY_DELIMITER = "&";

    private static final String KEY_VALUE_DELIMITER = "=";

    private final Values values;

    public RequestBody(String body) {
        this.values = convertToValues(body);
    }

    private Values convertToValues(String body) {
        if (Objects.isNull(body)) {
            return null;
        }

        return new StringValues(body, BODY_DELIMITER, KEY_VALUE_DELIMITER);
    }

    public String get(String key) {
        return values.get(key);
    }
}
