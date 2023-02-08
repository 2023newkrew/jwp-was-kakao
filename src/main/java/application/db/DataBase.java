package application.db;

import application.domain.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private static Map<String, User> users = new HashMap<>();
    static {
        users.put("jin.100", new User("jin.100", "password", "jin", "jin.100@kakaocorp.com"));
    }

    private DataBase(){}

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
