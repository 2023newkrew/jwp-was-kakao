package http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSessions {
    public static final String SESSION_KEY = "JSESSIONID";
    private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    private HttpSessions() {
    }

    public static HttpSession getSession(String id) {
        if (!sessions.containsKey(id)) {
            sessions.put(id, new HttpSession(id));
        }
        return sessions.get(id);
    }
}
