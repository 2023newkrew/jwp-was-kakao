package db;

import model.User;
import model.UserRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataBase {
    private static final Map<Long, User> users = new HashMap<>();
    private static Long autoIncrementId = 1L;

    private DataBase() {
    }

    public static synchronized Optional<Long> addUser(UserRequest userRequest) {
        users.put(autoIncrementId, userRequest.toEntity(autoIncrementId));

        return Optional.of(autoIncrementId++);
    }

    public static Optional<User> findUserByUserId(String userId) {
        try {
            return Optional.of(users.values().stream().filter(it -> it.getUserId().equals(userId)).collect(Collectors.toList()).get(0));
        } catch (IndexOutOfBoundsException e) {
            return Optional.ofNullable(null);
        }
    }

    public static User findUserById(Long id) {
        return users.get(id);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
