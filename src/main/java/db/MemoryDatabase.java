package db;

import model.User;

import java.util.Collection;
import java.util.Map;

public class MemoryDatabase implements Database {
    private final Map<String, User> users;

    public MemoryDatabase(Map<String, User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User findUserById(String userId) {
        return users.get(userId);
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
