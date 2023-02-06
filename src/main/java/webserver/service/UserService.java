package webserver.service;

import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.dao.UserDao;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(HttpRequest request) {
        userDao.saveUser(request);
    }
}
