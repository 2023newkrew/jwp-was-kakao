package webserver.session;

import http.HttpSession;

import java.util.HashMap;
import java.util.Map;

public class HttpSessionManager {
    public static final String SESSION_ID = "JSESSIONID";

    private static final Map<String, HttpSession> sessions = new HashMap<>();

    private HttpSessionManager() {
    }

    public static void add(HttpSession session) {
        sessions.put(session.getId(), session);
    }

    public static HttpSession findSession(String id) {
        return sessions.get(id);
    }

    public static void remove(String id) {
        sessions.remove(id);
    }

}
