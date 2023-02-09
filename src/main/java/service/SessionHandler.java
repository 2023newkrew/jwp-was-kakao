package service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandler {

    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    private static class LazyHolder {
        public static final SessionHandler INSTANCE = new SessionHandler();
    }

    private SessionHandler() {
    }

    public static SessionHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void saveSession(UUID uuid, Session session) {
        sessions.put(uuid, session);
    }

    public Session getSession(UUID uuid) {
        return sessions.get(uuid);
    }
}
