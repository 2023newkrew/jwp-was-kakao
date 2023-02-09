package db;

import model.User;
import model.UserRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataBase {
    private static final Map<Long, User> users = new HashMap<>();
    private static final Map<String, User> userIdIndex = new HashMap<>();
    private static Long autoIncrementId = 1L;
    public static synchronized Optional<Long> addUser(UserRequest userRequest) {
        User user = userRequest.toEntity(autoIncrementId);
        users.put(autoIncrementId, user);
        userIdIndex.put(user.getUserId(), user);

        return Optional.of(autoIncrementId++);
    }

    public static Optional<User> findUserByUserId(String userId) {
        if (!userIdIndex.containsKey(userId)) {
            return Optional.empty();
        }
        return Optional.of(userIdIndex.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
