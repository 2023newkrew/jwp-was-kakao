package was.domain.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, Session> SESSIONS = new HashMap<>();

    private SessionManager() {
    }

    public static Session createSession() {
        UUID uuid = UUID.randomUUID();
        Session session = new Session(uuid.toString());
        SESSIONS.put(uuid.toString(), session);

        return session;
    }

    public static Session getSession(String sessionId) {
        return SESSIONS.getOrDefault(sessionId, null);
    }
}
