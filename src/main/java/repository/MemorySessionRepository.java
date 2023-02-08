package repository;

import model.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MemorySessionRepository implements SessionRepository {
    private static final ConcurrentMap<String, User> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void put(String key, User user) {
        sessionMap.put(key, user);
    }

    @Override
    public User get(String key) {
        return sessionMap.get(key);
    }

    @Override
    public boolean containsKey(String uuid) {
        return sessionMap.containsKey(uuid);
    }
}
