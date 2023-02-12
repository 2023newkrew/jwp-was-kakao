package service;

import db.DataBase;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    public static void addUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
    }

    public static boolean login(String userId, String password) {
        Optional<User> user = DataBase.findByUserId(userId);
        return user.map(value -> value.isCorrectPassword(password)).orElse(false);
    }

    public static List<User> showUserList() {
        return new ArrayList<>(DataBase.findAll());
    }
}
