package webserver.controller;

import db.DataBase;
import model.User;
import webserver.request.HttpRequest;
import webserver.request.QueryStringParser;
import webserver.response.HttpResponse;
import webserver.security.Session;
import webserver.security.SessionManager;
import webserver.utils.ResponseUtil;

import javax.servlet.http.Cookie;
import java.util.Map;

import static webserver.request.HttpRequestMethod.POST;
import static webserver.request.QueryStringParser.getQuery;

public class LoginController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        Map<String, String> attributes = QueryStringParser.parseQueryString(getQuery(request), "&");

        User user = DataBase.findUserById(attributes.get("userId"));
        if (user == null || user.isWrongPassword(attributes.get("password"))) {
            ResponseUtil.response302(response, "/user/login_failed.html");
            return;
        }

        Session session = new Session(Map.of(user.getUserId(), user));
        SessionManager.getInstance().add(session.getKey(), session);
        response.addCookie(new Cookie("JSESSIONID", session.getKey()));
        ResponseUtil.response302(response, "/index.html");
    }

    @Override
    public boolean isMatch(HttpRequest request) {
        return request.getMethod() == POST && request.getUri().getPath().equals("/user/login");
    }
}
