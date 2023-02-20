package webserver.security;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private final Map<String, Object> values;
    private final String id;

    public Session(String id, Map<String, Object> attributes) {
        this.id = id;
        this.values = new HashMap<>(attributes);
    }

    public String getId() {
        return id;
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

