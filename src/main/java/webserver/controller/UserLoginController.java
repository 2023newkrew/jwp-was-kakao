package webserver.controller;

import db.SessionStorage;
import db.UserStorage;
import java.util.Map;
import java.util.UUID;
import model.MyHttpRequest;
import model.MyHttpResponse;
import model.Session;
import model.User;
import org.springframework.http.HttpStatus;
import webserver.Controller;
import webserver.MyHttpCookie;

public class UserLoginController implements Controller {

    public static final String URL = "/user/login";

    @Override
    public String process(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        Map<String, String> requestBody = httpRequest.getBody();
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");

        User user = UserStorage.findUserById(userId);

        httpResponse.setStatus(HttpStatus.FOUND);

        if (!user.isCorrectPassword(password)) {
            httpResponse.setLocation("/user/login_failed.html");
            return "";
        }

        String uuid = UUID.randomUUID().toString();
        createSession(user, uuid);
        createCookie(httpResponse, uuid);
        httpResponse.setLocation("/index.html");

        return "";
    }

    private void createSession(User user, String uuid) {
        Session session = new Session(uuid);
        session.setAttribute("user", user);
        SessionStorage.add(uuid, session);
    }

    private void createCookie(MyHttpResponse httpResponse, String uuid) {
        MyHttpCookie cookie = new MyHttpCookie("JSESSIONID", uuid);
        cookie.setPath("/");
        httpResponse.setCookie(cookie);
    }
}
