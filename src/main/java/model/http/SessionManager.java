package model.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SessionManager {
    private static SessionManager sessionManager = new SessionManager();

    private SessionManager() {}

    public static SessionManager getInstance() {
        return sessionManager;
    }

    private static final Map<String, CustomHttpSession> SESSIONS = new HashMap<>();

    public void add(final CustomHttpSession session) {
        SESSIONS.put(session.getId(), session);
    }

    public Optional<CustomHttpSession> findSession(final String id) {
        return Optional.of(SESSIONS.getOrDefault(id, null));
    }

    public void remove(final String id) {
        SESSIONS.remove(id);
    }

}
