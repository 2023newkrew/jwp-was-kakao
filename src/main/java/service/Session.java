package service;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final Map<String, String> session;

    public Session(Map<String, String> session) {
        this.session = session;
    }

    public Session() {
        this.session = new HashMap<>();
    }

    public String put(String key, String value) {
        return session.put(key, value);
    }

    public String get(String key) {
        return session.get(key);
    }
}
