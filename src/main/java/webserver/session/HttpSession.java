package webserver.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

@Getter
public class HttpSession {
    public static final String SESSION_ID_NAME = "JSESSIONID";
    private final Map<String, Object> values = new HashMap<>();
    private final String id;

    public HttpSession() {
        this(UUID.randomUUID().toString());
    }

    public HttpSession(String id) {
        this.id = id;
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

    public void invalidate() {
        HttpSessionManager.remove(id);
    }
}
