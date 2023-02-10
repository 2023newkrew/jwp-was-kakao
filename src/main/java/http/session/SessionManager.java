package http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SessionManager {

    public static final String SESSION_ID_NAME = "JSESSIONID";
    public static final String USER_NAME = "USER";
    private static final Map<String, Session> sessions = new HashMap<>();

    private SessionManager() {}

    public static void add(Session session) {
        sessions.put((String) session.getAttribute(SESSION_ID_NAME), session);
    }

    public static Optional<Session> findSession(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public static void remove(String sessionId) {
        sessions.remove(sessionId);
    }

}
