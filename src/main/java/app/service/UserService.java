package app.service;

import app.db.DataBase;
import app.model.User;

public class UserService {
    public void createUser(String userId, String password, String name, String email) {
        DataBase.addUser(new User( userId, password, name, email));
    }
}
