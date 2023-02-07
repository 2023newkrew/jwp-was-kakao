package repository;


import lombok.extern.slf4j.Slf4j;
import model.User;

import java.util.Collection;
import java.util.Optional;
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
    public Optional<User> findUserById(String userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        }
        return Optional.empty();

    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }
}
