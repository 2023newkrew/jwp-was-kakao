package db;


import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private static final Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        int currentId = users.size();
        user.setId(currentId + 1);
        users.put(user.getUserId(), user);
        System.out.println("ADD USER = " + user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static User findUserByIdAndPassword(String userId, String password) {
        if (!users.containsKey(userId)) {
            return null;
        }
        User user = users.get(userId);
        if (user.hasSamePassword(password)) {
            return user;
        }
        return null;
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
