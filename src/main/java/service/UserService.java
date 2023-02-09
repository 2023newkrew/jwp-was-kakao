package service;

import db.DataBase;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    public void addUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
    }

    public boolean loginUser(String userId, String password) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public List<User> getUsers() {
        return new ArrayList<>(DataBase.findAll());
    }
}
