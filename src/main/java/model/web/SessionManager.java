package model.web;

import exception.NoSuchSessionException;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class SessionManager {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void createSession(String id, final Session session) {
        sessions.put(id, session);
    }

    public Session findSession(final String id) {
        try {
            return sessions.get(id);
        } catch (Exception e) {
            throw new NoSuchSessionException(e);
        }
    }

    public void setCookieInSession(Cookie cookie, Session session, Object object) {
        SessionManager.createSession(cookie.getKey(), session);
        session.setAttribute(cookie.getValue(), object);
    }

    public void remove(final String id) {
        sessions.remove(id);
    }

}
