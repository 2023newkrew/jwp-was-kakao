package service;

import db.DataBase;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    public void addUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
    }

    public boolean login(String userId, String password) {
        Optional<User> user = DataBase.findByUserId(userId);
        return user.map(value -> value.checkPassword(password)).orElse(false);
    }

    public List<User> showList() {
        return new ArrayList<>(DataBase.findAll());
    }
}
