package db;

import lombok.extern.slf4j.Slf4j;
import model.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DataBase {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        log.info("Added " + user.getName());
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
