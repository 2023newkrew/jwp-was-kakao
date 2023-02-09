package webserver.http.header;

import webserver.collection.StringValues;
import webserver.collection.Values;

import java.util.List;
import java.util.stream.Collectors;

public class Headers {
    private static final String HEADER_DELIMITER = ":";

    private final Values values;

    public Headers() {
        values = new StringValues();
    }

    public Headers(List<String> headers) {
        values = new StringValues(headers, HEADER_DELIMITER);
    }

    public void put(HeaderType key, Object value) {
        values.put(key.getName(), value.toString());
    }

    public String get(HeaderType key) {
        return values.get(key.getName());
    }

    @Override
    public String toString() {
        return values.keySet()
                .stream()
                .map(key -> key + ": " + values.get(key))
                .collect(Collectors.joining("\r\n", "", "\r\n\r\n"));
    }
}
