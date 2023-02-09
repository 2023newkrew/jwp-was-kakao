package db;

import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DataBase {

    private static final Map<String, User> users = new HashMap<>();

    static {
        users.put("cu", new User("cu", "asdf", "dfsa", "fdsaf@fd.scom"));
        users.put("bsd", new User("bsd", "asdf", "dfsa", "fdsaf@fd.scom"));
    }

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
