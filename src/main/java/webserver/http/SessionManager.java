package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private final Map<String, Session> sessions = new HashMap<>();

    private SessionManager() {
    }

    private static class SessionManagerHolder {
        private static SessionManager INSTANCE = new SessionManager();
    }

    public static SessionManager getInstance() {
        return SessionManagerHolder.INSTANCE;
    }

    public void add(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }

    public boolean isValid(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public Session findSession(String sessionId) {
        return sessions.get(sessionId);
    }
}
