package model.http;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpSession {

    private final String id;
    private final Map<String, Object> values = new HashMap<>();

    public CustomHttpSession(final String id) {
        this.id = id;
        SessionManager.getInstance().add(this);
    }

    public String getId() { return id; }

    public Object getAttribute(final String name) {
        return values.getOrDefault(name, null);
    }

    public void setAttribute(final String name, final Object value) {
        values.put(name, value);
    }

    public void removeAttribute(final String name) {
        values.computeIfPresent(name, (String, Void) -> values.remove(name));
    }

    public void invalidate() {
        values.keySet().removeAll(values.keySet());
    }
}