package db;


import model.User;

import java.util.*;

public class DataBase {
    private static final Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        int currentId = users.size();
        user.setId(currentId + 1);
        users.put(user.getUserId(), user);
    }

    public static User findUserByIdAndPassword(String userId, String password) {
        if (!users.containsKey(userId)) {
            return null;
        }
        User user = users.get(userId);
        if (!user.hasSamePassword(password)) {
            return null;
        }
        return user;
    }

    public static List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
