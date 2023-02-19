package service;

import common.HttpRequest;
import model.User;
import support.LoginFailedException;
import webserver.Session;
import webserver.SessionManager;

import java.util.UUID;

public class AuthService {
    private final SessionManager sessionManager;

    public static final String SESSION_COOKIE = "JSESSIONID";


    public AuthService(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public boolean isAuthenticated(HttpRequest request) {
        return request.getCookie(SESSION_COOKIE).isPresent() &&
                sessionManager.get(request.getCookie(SESSION_COOKIE).get()).isPresent();
    }

    public String login(User user, String password) {
        if (!user.getPassword().equals(password)) {
            throw new LoginFailedException();
        }
        Session session = new Session(UUID.randomUUID().toString());
        session.setAttribute("user", user);
        sessionManager.add(session);
        return session.getId();
    }
}
