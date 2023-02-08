package webserver.http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, HttpSession> sessions = new HashMap<>();

    private SessionManager() {}

    public static HttpSession create() {
        String id = UUID.randomUUID().toString();
        HttpSession session = new HttpSession(id);
        sessions.put(id, session);
        return session;
    }

    public static Optional<HttpSession> findSession(String id) {
        return Optional.ofNullable(sessions.get(id));
    }

    public static void remove(String id) {
        sessions.remove(id);
    }
}
