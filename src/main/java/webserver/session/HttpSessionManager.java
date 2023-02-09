package webserver.session;

import java.util.HashMap;
import java.util.Map;

public class HttpSessionManager {
    private static final Map<String, HttpSession> SESSIONS = new HashMap<>();

    public static void add(HttpSession httpSession) {
        SESSIONS.put(httpSession.getId(), httpSession);
    }

    public static HttpSession findHttpSession(String id) {
        if (SESSIONS.containsKey(id)) {
            return SESSIONS.get(id);
        }
        return createSession(id);
    }

    public static HttpSession createSession() {
        HttpSession httpSession = new HttpSession();
        SESSIONS.put(httpSession.getId(), httpSession);
        return httpSession;
    }

    public static HttpSession createSession(String id) {
        HttpSession httpSession = new HttpSession(id);
        SESSIONS.put(id, httpSession);
        return httpSession;
    }

    public static void remove(String id) {
        SESSIONS.remove(id);
    }

    private HttpSessionManager() {
    }
}
