package service;

import db.DataBase;
import model.User;
import support.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    public void addUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
    }

    public List<User> getUsers() {
        return new ArrayList<>(DataBase.findAll());
    }

    public User getUser(String userId) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
