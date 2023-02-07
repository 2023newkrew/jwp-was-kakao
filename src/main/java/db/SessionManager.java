package db;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import static constant.SessionUUID.*;

@UtilityClass
public class SessionManager {
    private static final Map<String, Session> SESSIONS = new HashMap<>();

    static {
        SESSIONS.put(USER_SESSION_UUID, new UserSession());
    }

    public void add(final Session session) {
        SESSIONS.put(session.getId(), session);
    }

    public Session findSession(final String id) {
        try {
            return SESSIONS.get(id);
        } catch (Exception e) {
            throw new RuntimeException("해당 세션이 존재하지 않습니다.");
        }
    }

    public void remove(final String id) {
        SESSIONS.remove(id);
    }
}
