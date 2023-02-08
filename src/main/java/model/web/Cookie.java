package model.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cookie {
    private final String key;
    private final String value;

    public Cookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String key() {
        return key;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
