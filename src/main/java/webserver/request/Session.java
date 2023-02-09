package webserver.request;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session {

    private final String sessionId;
    private final Map<String, Object> values = new HashMap<>();

    public Session() {
        this.sessionId = String.valueOf(UUID.randomUUID());
    }

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Object getAttribute(String name) {
        return values.get(name);
    }

    public void setAttribute(String name, Object value) {
        values.put(name, value);
    }

    public void removeAttribute(String name) {
        values.remove(name);
    }
}
