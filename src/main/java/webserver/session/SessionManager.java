package webserver.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private static final Map<String, Session> sessionRepository = new HashMap<>();

    public static Session create() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId);
        save(session);
        return session;
    }

    private static void save(Session session) {
        sessionRepository.put(session.getId(), session);
    }

    public static Session findSession(String id) {
        return sessionRepository.get(id);
    }

}
