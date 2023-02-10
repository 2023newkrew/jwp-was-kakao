package application.db;

import application.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static List<User> findAll() {
        return List.copyOf(users.values());
    }
}
