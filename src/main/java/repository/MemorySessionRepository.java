package repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MemorySessionRepository implements SessionRepository {
    private static final ConcurrentMap<String, String> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void put(String key, String value) {
        sessionMap.put(key, value);
    }

    @Override
    public String get(String key) {
        return sessionMap.get(key);
    }
}
