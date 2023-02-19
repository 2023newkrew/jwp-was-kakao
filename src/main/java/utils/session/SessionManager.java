package utils.session;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    public static final SessionManager instance = new SessionManager();
    private final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();

    // Singleton
    private SessionManager(){}

    public void add(final Session session){
        SESSIONS.put(session.getId(), session);
    }

    public Session findSession(final String id){
        Session result = SESSIONS.get(id);
        if (Objects.isNull(result) || result.isExpired()){
            remove(id);
            return null;
        }
        result.renewExpiry();
        return result;
    }

    public void remove(final String id){
        SESSIONS.remove(id);
    }
}
