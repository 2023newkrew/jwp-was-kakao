package service;

import db.DataBase;
import model.User;

public class UserService {
    public static boolean isValidAuthentication(String userId, String password) {
        User userById = DataBase.findUserById(userId);
        if (userById == null) {
            return false;
        }
        return userById.getPassword().equals(password);
    }

    public static void saveUser(User newUser) {
        DataBase.addUser(newUser);
    }

    public static User findUserById(String id) {
        return DataBase.findUserById(id);
    }
}
