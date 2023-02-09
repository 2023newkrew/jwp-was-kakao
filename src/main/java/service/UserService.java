package service;

import db.DataBase;
import model.User;

import java.util.Map;

public class UserService {

    private static class LazyHolder {
        public static final UserService INSTANCE = new UserService();
    }

    private UserService() {
    }

    public static UserService getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void createUser(Map<String, String> params) {
        User user = User.from(params);
        DataBase.addUser(user);
    }
}
