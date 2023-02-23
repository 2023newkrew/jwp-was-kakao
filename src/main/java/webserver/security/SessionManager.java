package webserver.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SessionManager {

    private static final Map<String, Session> SESSIONS = new HashMap<>();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void add(final String key, final Session session) {
        SESSIONS.put(key, session);
    }

    public Optional<Session> findSession(final String id) {
        return Optional.ofNullable(SESSIONS.get(id));
    }

    public void remove(final String id) {
        SESSIONS.remove(id);
    }

    private static class LazyHolder {

        private static final SessionManager INSTANCE = new SessionManager();
    }
}
