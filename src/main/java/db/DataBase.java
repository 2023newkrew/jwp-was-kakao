package db;

import java.util.Collection;
import model.User;

public class DataBase {

    public static User addUser(User user) {
        return UserDataBase.insert(user);
    }

    public static User findUserById(String userId) {
        return UserDataBase.findByUserId(userId);
    }

    public static Collection<User> findAll() {
        return UserDataBase.findAll();
    }
}
