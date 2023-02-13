package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SessionManager {
    private static final Map<String, Session> sessions = new HashMap<>();

    public void add(Session session) {
        sessions.put(session.getId(), session);
    }

    public Optional<Session> get(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }
}
