package auth;

import model.User;

import java.util.concurrent.ConcurrentHashMap;

public class Session {

    private final String id;
    private final ConcurrentHashMap<String, User> values = new ConcurrentHashMap<>();

    public Session(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public User getAttribute(String name) {
        return values.get(name);
    }

    public void setAttribute(String name, User value) {
        values.put(name, value);
    }

    public void removeAttribute(String name) {
        values.remove(name);
    }

    public void invalidate() {
        values.clear();
    }
}