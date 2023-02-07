package db;

import model.User;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class DataBase {

    private static ConcurrentMap<String, User> users = new ConcurrentReferenceHashMap<>();

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
