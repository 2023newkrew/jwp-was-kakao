package service;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final Map<String, Object> session;

    public Session(Map<String, Object> session) {
        this.session = session;
    }

    public Session() {
        this.session = new HashMap<>();
    }

    public Object put(String key, Object value) {
        return session.put(key, value);
    }

    public Object get(String key) {
        return session.get(key);
    }
}
