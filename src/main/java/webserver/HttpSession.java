package webserver;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSession {

    private final String id;
    private final Map<String, Object> values = new HashMap<>();
    private LocalDateTime expireDateTime;

    public HttpSession() {
        this.id = UUID.randomUUID().toString();
        this.expireDateTime = LocalDateTime.now().plusMinutes(30);
    }

    public String getId() {
        return id;
    }

    public Object getAttribute(final String key) {
        return values.get(key);
    }
    public LocalDateTime getExpireDateTime() {
        return expireDateTime;
    }

    public void setAttribute(final String name, final Object value) {
        values.put(name, value);
    }

    public void removeAttribute(final String name) {
        values.remove(name);
    }

    public void invalidate() {
        values.clear();
    }
}