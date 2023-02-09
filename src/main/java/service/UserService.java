package service;

import db.DataBase;
import framework.request.HttpCookie;
import model.User;

import java.util.Map;
import java.util.UUID;

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

    public HttpCookie loginUser(Map<String, String> params) {
        User user = DataBase.findUserById(params.get("userId"));
        if (user == null) {
            throw new RuntimeException();
        }
        if (user.checkPassword(params.get("password"))) {
            UUID uuid = UUID.randomUUID();
            Session session = new Session(Map.of("user", user));
            SessionHandler.getInstance().saveSession(uuid, session);
            return HttpCookie.from(Map.of("JSESSIONID", uuid.toString()));
        }
        throw new RuntimeException();
    }

    public User getUserFromCookie(HttpCookie cookie) {
        try {
            UUID uuid = UUID.fromString(cookie.get("JSESSIONID"));
            Session session = SessionHandler.getInstance().getSession(uuid);
            return (User) session.get("user");
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException();
        }
    }

    public boolean isUserLoggedIn(HttpCookie cookie) {
        if (cookie == null || cookie.get("JSESSIONID") == null) {
            return false;
        }
        UUID uuid = UUID.fromString(cookie.get("JSESSIONID"));
        Session session = SessionHandler.getInstance().getSession(uuid);
        return session != null && session.get("user") != null;
    }
}
