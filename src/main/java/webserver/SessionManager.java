package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, HttpSession> SESSIONS = new HashMap<>();

    public static HttpSession createSession() {
        String sessionId = UUID.randomUUID().toString();
        HttpSession session = new HttpSession(sessionId);
        SESSIONS.put(sessionId, session);
        return session;
    }

    public static HttpSession getSession(String sessionId) {
        return SESSIONS.getOrDefault(sessionId, null);
    }
}
