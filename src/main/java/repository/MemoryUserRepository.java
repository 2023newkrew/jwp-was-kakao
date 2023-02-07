package repository;


import lombok.extern.slf4j.Slf4j;
import model.User;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class MemoryUserRepository implements UserRepository {
    private static final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
        log.info("ADD USER = " + user);
    }

    @Override
    public User findUserById(String userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }
}
