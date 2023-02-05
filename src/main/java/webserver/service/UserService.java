package webserver.service;

import model.HttpRequest;
import webserver.dao.UserDao;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(HttpRequest request) {
        userDao.saveUser(request);
    }
}
