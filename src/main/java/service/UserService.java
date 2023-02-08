package service;

import db.DataBase;
import model.User;

public class UserService {

    public void saveUser(User user) {
        DataBase.addUser(user);
    }
}
