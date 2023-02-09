package web.service;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final Map<String, Object> session;

    public Session(Map<String, Object> session) {
        this.session = session;
    }

    public Object get(String key) {
        return session.get(key);
    }
}
