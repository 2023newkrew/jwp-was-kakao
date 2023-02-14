package db;

import model.User;
import model.UserRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class DataBase {
    private static final Map<Long, User> users = new HashMap<>();
    private static final AtomicLong autoIncrementId = new AtomicLong(1L);

    public static Optional<Long> addUser(UserRequest userRequest) {
        Long id = autoIncrementId.getAndIncrement();

        User user = userRequest.toEntity(id);
        users.put(id, user);

        return Optional.of(id);
    }

    public static Optional<User> findUserByUserId(String userId) {
        return users.values().stream()
                .filter(it -> userId.equals(it.getUserId()))
                .findAny();
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
