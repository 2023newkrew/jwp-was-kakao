package webserver.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSessions {

    private static final Map<String, HttpSession> SESSIONS = new HashMap<>();

    private HttpSessions() {
    }

    public static HttpSession get(String sessionId) {
        return SESSIONS.get(sessionId);
    }

    public static HttpSession create() {
        UUID uuid = UUID.randomUUID();
        String sessionId = uuid.toString();
        HttpSession httpSession = new HttpSession(sessionId);
        SESSIONS.put(sessionId, httpSession);
        return httpSession;
    }

    public static Object findValue(String sessionId, String key) {
        if (!SESSIONS.containsKey(sessionId)) {
            return null;
        }
        HttpSession httpSession = SESSIONS.get(sessionId);
        return httpSession.getAttribute(key);
    }
}
