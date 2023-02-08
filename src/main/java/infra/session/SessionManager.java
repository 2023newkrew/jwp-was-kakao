package infra.session;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private static final Map<String, Session> SESSIONS = new HashMap<>();

    public void add(final Session session) {
        SESSIONS.put(session.getId(), session);
    }

    public Session findSession(final String id) {
        return SESSIONS.get(id);
    }

    public void remove(final String id) {
        SESSIONS.remove(id);
    }

    private SessionManager() {
    }

    private static class LazyHolder{
        private static final SessionManager instance = new SessionManager();
    }

    public static SessionManager getInstance(){
        return LazyHolder.instance;
    }

}
