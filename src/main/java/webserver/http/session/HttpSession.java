package webserver.http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpSession {
    public static final String JSESSIONID = "JSESSIONID";
    public static final String USER = "USER";
    private final String id;
    private final Map<String, Object> values = new HashMap<>();

    public HttpSession(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Optional<Object> getAttribute(String key) {
        return Optional.ofNullable(values.get(key));
    }

    public void setAttribute(String key, Object value) {
        values.put(key, value);
    }

    public void removeAttribute(String key) {
        values.remove(key);
    }

    public void invalidate() {
        values.clear();
    }
}
