package auth;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final ConcurrentHashMap<String, Session> Sessions = new ConcurrentHashMap<>();

    public void add(Session session, HttpCookie httpCookie) {
        Sessions.put(httpCookie.getCookie(), session);
    }

    public Session findSession(String id) {
        return Sessions.get(id);
    }

    public void remove(String id) {
        Sessions.remove(id);
    }
}
