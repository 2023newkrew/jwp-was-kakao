package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    public static final String SESSION_ID_NAME = "JSESSIONID";
    private static final Map<String, Session> sessions = new HashMap<>();

    public static Session getSession(String id) {
        Session session = sessions.get(id);
        if(session == null) {
            session = new Session(id);
            sessions.put(id, session);
        }
        return session;
    }

    public static void remove(final String id) {
        sessions.remove(id);
    }

    public static String getStatus() {
        String s = "SessionManager --> ";
        for(String key : sessions.keySet()) {
            s+=(key + " : " + sessions.get(key));
        }
        return s;
    }
}
