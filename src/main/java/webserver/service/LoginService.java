package webserver.service;

import model.request.HttpRequest;
import model.user.User;
import webserver.dao.UserDao;

import java.util.UUID;

public class LoginService {
    private final UserDao userDao;

    public LoginService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UUID login(HttpRequest request) {
        User user = userDao.findUserByUserId(request.getBodyValue("userId"));
        if (!user.passwordEquals(request.getBodyValue("password"))) {
            // todo: 비밀번호 불일치 로직 처리
            throw new RuntimeException("비밀번호 불일치");
        }

        return UUID.randomUUID();
    }
}
