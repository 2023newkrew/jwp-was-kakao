package http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, Session> SESSIONS = new HashMap<>();

    private SessionManager() {
    }

    private static class SessionManagerHolder {
        private static final SessionManager instance = new SessionManager();
    }

    public static SessionManager getInstance() {
        return SessionManagerHolder.instance;
    }

    public void add(final Session session) {
        SESSIONS.put(session.getId(), session);
    }

    public Session newSession() {
        UUID uuid = UUID.randomUUID();
        String sessionId = uuid.toString();
        Session session = new Session(sessionId);
        add(session);
        return session;
    }

    public Session findSession(final String id) {
        return SESSIONS.getOrDefault(id, null);
    }

    public void remove(final String id) {
        SESSIONS.remove(id);
    }
}
