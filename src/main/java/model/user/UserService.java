package model.user;

import db.DataBase;

public class UserService {
    public void save(User user){
        DataBase.addUser(user);
    }
}
