package db;

import model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    private static final Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    /* 2단계에서 사용 예정 */
    @SuppressWarnings("unused")
    public static User findUserById(String userId) {
        return users.get(userId);
    }

    /* 2단계에서 사용 예정 */
    @SuppressWarnings("unused")
    public static Collection<User> findAll() {
        return users.values();
    }
}
