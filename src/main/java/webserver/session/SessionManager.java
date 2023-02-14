package webserver.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();

    public static SessionManager getInstance() {
        return instance;
    }

    private final Map<String, Session> sessions = new HashMap<>();

    private SessionManager() {}

    public Session create() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId);
        sessions.put(sessionId, session);

        return session;
    }

    public Session get(String sessionId) {
        return sessions.get(sessionId);
    }

    public void delete(String sessionId) {
        sessions.put(sessionId, null);
    }
}
