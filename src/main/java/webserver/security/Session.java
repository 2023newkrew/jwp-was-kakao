package webserver.security;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session {

    private final Map<String, Object> values;
    private final String key;

    public Session(Map<String, Object> attributes) {
        this.key = UUID.randomUUID().toString();
        this.values = new HashMap<>(attributes);
    }

    public String getKey() {
        return key;
    }

    public Object getAttribute(final String name) {
        return values.get(name);
    }

    public void removeAttribute(final String name) {
        values.remove(name);
    }

    public void invalidate() {
        values.clear();
    }
}

