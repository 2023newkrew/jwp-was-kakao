package webserver.common;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    private final String id;
    private final Map<String, Object> sessionValues;

    public HttpSession(String id) {
        this.id = id;
        this.sessionValues = new HashMap<>();
    }

    public void setAttribute(String key, Object value) {
        sessionValues.put(key, value);
    }

    public String getId() {
        return id;
    }

    public Object getAttribute(String key) {
        return sessionValues.get(key);
    }
}
