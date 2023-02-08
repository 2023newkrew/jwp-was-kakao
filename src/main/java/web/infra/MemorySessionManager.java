package web.infra;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MemorySessionManager implements SessionManager {

    private final static Map<String, Session> sessions = new ConcurrentHashMap<>();

    private long expirationTime;

    @Override
    public void setAttribute(String key, Object value) {
        sessions.put(key, new Session(value, expirationTime));
    }

    @Override
    public Object getAttribute(String sessionId) {
        if (sessions.get(sessionId).isExpired()) {
            removeAttribute(sessionId);
            return null;
        }

        return sessions.get(sessionId).getValue();
    }

    @Override
    public void removeAttribute(String sessionId) {
        sessions.remove(sessionId);
    }

    @Override
    public void setExpirationTime(long expirationTime, TimeUnit unit) {
        this.expirationTime = unit.toSeconds(expirationTime);
    }

    @Override
    public long getExpirationTime() {
        return expirationTime;
    }
}
