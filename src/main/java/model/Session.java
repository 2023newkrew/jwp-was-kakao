package model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Session {
    @Getter
    private final String id;
    private final ConcurrentMap<String, Object> values = new ConcurrentHashMap<>();

    public Object getAttribute(String key) {
        return values.get(key);
    }

    public void setAttribute(String key, Object value) {
        values.put(key, value);
    }

    public void removeAttribute(String key) {
        values.remove(key);
    }

    public void invalidate() {
        values.clear();
    }

    public static Session of(User user) {
        String id = UUID.randomUUID().toString();
        Session session = new Session(id);
        session.setAttribute("user", user);

        return session;
    }
}
