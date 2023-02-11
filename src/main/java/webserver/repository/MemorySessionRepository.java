package webserver.repository;

import webserver.model.Session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MemorySessionRepository implements SessionRepository {
    private static final ConcurrentMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void save(Session session) {
        sessionMap.put(session.getId(), session);
    }

    @Override
    public Session findById(String id) {
        return sessionMap.get(id);
    }

    @Override
    public boolean isExist(String id) {
        return sessionMap.containsKey(id);
    }

    @Override
    public void remove(String id) {
        sessionMap.remove(id);
    }
}
