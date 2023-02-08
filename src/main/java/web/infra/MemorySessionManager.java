package web.infra;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemorySessionManager implements SessionManager {

    private final static Map<String, Object> sessions = new ConcurrentHashMap<>();

    @Override
    public void setAttribute(String key, Object value) {
        sessions.put(key, value);
    }

    @Override
    public Optional<Object> getAttribute(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @Override
    public void remoteAttribute(String sessionId) {
        sessions.remove(sessionId);
    }

}
