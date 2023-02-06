package webserver.service;

import model.request.HttpRequest;
import model.user.User;
import webserver.dao.UserDao;

import java.util.Optional;
import java.util.UUID;

public class LoginService {
    private final UserDao userDao;

    public LoginService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<UUID> login(HttpRequest request) {
        User user = userDao.findUserByUserId(request.getBodyValue("userId"));

        if (!user.passwordEquals(request.getBodyValue("password"))) {
            return Optional.empty();
        }

        return Optional.of(UUID.randomUUID());
    }
}
