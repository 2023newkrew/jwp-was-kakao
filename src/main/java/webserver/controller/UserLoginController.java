package webserver.controller;

import db.DataBase;
import http.Cookie;
import http.HttpMethod;
import http.HttpResponse;
import http.ResponseStatus;
import http.request.HttpRequest;
import http.request.RequestParam;
import http.session.Session;
import http.session.SessionManager;
import model.User;

import java.util.Optional;
import java.util.UUID;

public class UserLoginController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        if (request.getMethod() == HttpMethod.GET) {
            return processGetRequest(request, response);
        }
        if (request.getMethod() == HttpMethod.POST) {
            return processPostRequest(request, response);
        }
        throw new IllegalArgumentException();
    }

    private String processGetRequest(HttpRequest request, HttpResponse response) {
        if (request.isLoggedIn()) {
            response.setResponseStatus(ResponseStatus.FOUND);
            return "/index.html";
        }
        return "/user/login.html";
    }

    private String processPostRequest(HttpRequest request, HttpResponse response) {
        response.setResponseStatus(ResponseStatus.FOUND);
        RequestParam requestParam = request.getRequestParam();
        String userId = requestParam.get("userId");
        Optional<User> userQueryResult = DataBase.findUserById(userId);

        if (userQueryResult.isEmpty()) return "/user/login_failed.html";
        User user = userQueryResult.get();
        if (!user.matchPassword(requestParam.get("password"))) return "/user/login_failed.html";

        setCookie(response, user);
        return "/index.html";
    }

    private void setCookie(HttpResponse response, User user) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId);
        session.setAttribute(SessionManager.USER_NAME, user);
        SessionManager.add(session);

        response.setCookie(new Cookie(SessionManager.SESSION_ID_NAME, sessionId));
        response.setCookie(new Cookie(Cookie.PATH_NAME, "/"));
    }
}
