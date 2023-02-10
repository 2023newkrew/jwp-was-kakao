package webserver.collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringValues implements Values {

    private final Map<String, String> values = new HashMap<>();


    public StringValues() {}

    public StringValues(String valueLine, String delimiter, String keyValueDelimiter) {
        this(List.of(valueLine.split(delimiter)), keyValueDelimiter);
    }

    public StringValues(List<String> values, String keyValueDelimiter) {
        for (String value : values) {
            put(value.split(keyValueDelimiter, 2));
        }
    }

    private void put(String[] keyValue) {
        if (keyValue.length < 2) {
            return;
        }
        put(keyValue[0], keyValue[1]);
    }

    @Override
    public void put(String key, String value) {
        values.put(key.trim(), value.trim());
    }

    @Override
    public String get(String key) {
        return values.get(key);
    }

    @Override
    public Set<String> keySet() {
        return values.keySet();
    }
}
