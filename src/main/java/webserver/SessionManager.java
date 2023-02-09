package webserver;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static final Map<String, HttpSession> SESSIONS = new HashMap<>();

    public static void add(HttpSession session) {
        SESSIONS.put(session.getId(), session);
    }

    public static HttpSession getSession(String sessionId) {
        return SESSIONS.getOrDefault(sessionId, null);
    }
}
