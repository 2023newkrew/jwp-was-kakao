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
        Optional<User> user = userDao.findUserByUserId(request.getBodyValue("userId"));
        
        if (isInvalidLogin(request, user)) {
            return Optional.empty();
        }

        return Optional.of(UUID.randomUUID());
    }

    private boolean isInvalidLogin(HttpRequest request, Optional<User> user) { // todo: question
        return user.isEmpty() || !user.get().passwordEquals(request.getBodyValue("password"));
    }
}
