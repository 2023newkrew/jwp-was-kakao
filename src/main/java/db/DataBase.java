package db;

import model.User;
import model.UserRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataBase {
    private static Map<Long, User> users = new HashMap<>();
    private static Long autoIncrementId = 1L;
    public static synchronized Optional<Long> addUser(UserRequest userRequest) {
        users.put(autoIncrementId, userRequest.toEntity(autoIncrementId));

        return Optional.of(autoIncrementId++);
    }

    public static User findUserById(Long id) {
        return users.get(id);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
