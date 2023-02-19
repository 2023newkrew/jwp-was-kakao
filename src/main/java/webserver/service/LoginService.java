package webserver.service;

import model.request.HttpRequest;
import model.user.User;
import webserver.dao.UserDao;

import java.util.Optional;

public class LoginService {
    private final UserDao userDao;

    public LoginService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> login(HttpRequest request) {
        Optional<User> user = userDao.findUserByUserId(request.findBodyValue("userId", null));

        if (user.isEmpty() || isInvalidLogin(request, user.get())) {
            return Optional.empty();
        }

        return user;
    }

    private boolean isInvalidLogin(HttpRequest request, User user) {
        return !user.passwordEquals(request.findBodyValue("password", null));
    }
}
