package session;

import exceptions.InvalidSessionException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionManager {
    private static final Map<String, Session> SESSIONS = new HashMap<>();
    private static SessionManager instance;

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void add(final Session session) {
        SESSIONS.put(session.getId(), session);
    }

    public Session findSession(final String id) {
        return Optional.ofNullable(SESSIONS.get(id))
                .orElseThrow(InvalidSessionException::new);
    }

    public void remove(final String id) {
        SESSIONS.remove(id);
    }
}
