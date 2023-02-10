package auth;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public static void add(HttpCookie httpCookie, Session session) {
        sessions.put(httpCookie.getCookie(), session);
    }

    public static Session findSession(String id) {
        return sessions.get(id);
    }

    public static void remove(String id) {
        sessions.remove(id);
    }

    public static Set keySet(){
        return sessions.keySet();
    }

    public static Integer size(){
        return sessions.size();
    }
}
