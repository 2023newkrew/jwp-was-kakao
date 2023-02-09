package webserver.controller;

import static model.MyHttpRequest.JSESSIONID;

import db.SessionStorage;
import db.UserStorage;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.MyHttpRequest;
import model.MyHttpResponse;
import model.Session;
import model.User;
import org.springframework.http.HttpStatus;
import webserver.Controller;
import webserver.MyHttpCookie;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginController implements Controller {

    private static final UserLoginController INSTANCE = new UserLoginController();
    public static final String URL = "/user/login";
    private static final String REDIRECTION_FAILED = "/user/login_failed.html";
    private static final String REDIRECTION_SUCCESS = "/index.html";

    public static UserLoginController getInstance() {
        return INSTANCE;
    }

    @Override
    public String process(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        Map<String, String> requestBody = httpRequest.getBody();
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");

        User user = UserStorage.findUserById(userId);

        httpResponse.setStatus(HttpStatus.FOUND);

        if (!user.isCorrectPassword(password)) {
            httpResponse.setLocation(REDIRECTION_FAILED);
            return "";
        }

        String uuid = UUID.randomUUID().toString();
        createSession(user, uuid);
        createCookie(httpResponse, uuid);
        httpResponse.setLocation(REDIRECTION_SUCCESS);

        return "";
    }

    private void createSession(User user, String uuid) {
        Session session = new Session(uuid);
        session.setAttribute("user", user);
        SessionStorage.add(uuid, session);
    }

    private void createCookie(MyHttpResponse httpResponse, String uuid) {
        MyHttpCookie cookie = new MyHttpCookie(JSESSIONID, uuid);
        cookie.setPath("/");
        httpResponse.setCookie(cookie);
    }
}
