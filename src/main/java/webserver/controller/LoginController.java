package webserver.controller;

import db.DataBase;
import model.User;
import webserver.request.HttpRequest;
import webserver.request.QueryStringParser;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseStatus;
import webserver.security.Session;
import webserver.security.SessionManager;
import webserver.utils.ResponseUtil;

import javax.servlet.http.Cookie;
import java.util.Map;
import java.util.UUID;

import static webserver.request.HttpRequestMethod.POST;
import static webserver.request.QueryStringParser.getQuery;
import static webserver.response.HttpResponseStatus.REDIRECT;

public class LoginController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        Map<String, String> attributes = QueryStringParser.parseQueryString(getQuery(request), "&");

        User user = DataBase.findUserById(attributes.get("userId"));
        if (user == null || user.isWrongPassword(attributes.get("password"))) {
            ResponseUtil.response302(response, "/user/login_failed.html");
            return;
        }

        String sessionKey = UUID.randomUUID().toString();
        Session session = new Session(sessionKey);
        session.setAttribute(user.getUserId(), user);
        SessionManager.getInstance().add(sessionKey, session);
        response.addCookie(new Cookie("JSESSIONID", sessionKey));
        ResponseUtil.response302(response, "/index.html");
    }

    @Override
    public boolean isMatch(HttpRequest request) {
        return request.getMethod() == POST && request.getUri().getPath().equals("/user/login");
    }

    @Override
    public HttpResponseStatus getSuccessCode() {
        return REDIRECT;
    }
}
