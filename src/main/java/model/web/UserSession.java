package model.web;

import model.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserSession implements Session<User> {
    private final Map<String, User> attributes = new HashMap<>();


    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setAttribute(String key, User value) {
        attributes.put(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public void invalidate() {
        attributes.clear();
    }

    @Override
    public Optional<User> getAttribute(String key) {
        return Optional.ofNullable(
                attributes.getOrDefault(key, null)
        );
    }

}
