package db;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.Session;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionStorage {

    private static final Map<String, Session> SESSIONS = new HashMap<>();

    public static void add(String id, Session session) {
        SESSIONS.put(id, session);
    }

    public static Session findSession(String id) {
        return SESSIONS.get(id);
    }

    public static void remove(String id) {
        SESSIONS.remove(id);
    }
}
