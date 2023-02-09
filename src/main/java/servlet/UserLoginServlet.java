package servlet;

import datastructure.StringPair;
import db.DataBase;
import exception.BadRequestException;
import http.HttpStatus;
import http.cookie.HttpCookie;
import http.request.Request;
import http.request.RequestBody;
import http.response.Response;
import http.session.Session;
import http.session.SessionManager;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@ServletMapping(uri = "/user/login")
public class UserLoginServlet implements Servlet {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginServlet.class);
    private static final SessionManager sessionManager = SessionManager.getInstance();
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String JSESSIONID = "JSESSIONID";
    private static final String PATH = "Path";
    private static final String ROOT = "/";
    private static final String LOGIN_SUCCESS = "/index.html";
    private static final String LOGIN_FAIL = "/login_failed.html";

    private UserLoginServlet() {
    }

    private static class UserLoginServletHolder {
        private static final UserLoginServlet instance = new UserLoginServlet();
    }

    public static UserLoginServlet getInstance() {
        return UserLoginServletHolder.instance;
    }

    @Override
    public Response doPost(Request request) {
        RequestBody requestBody = request.getBody();
        String userId = requestBody.get(USER_ID).orElseThrow(BadRequestException::new);
        String password = requestBody.get(PASSWORD).orElseThrow(BadRequestException::new);
        User user = DataBase.findUserById(userId);

        if (authenticateUser(user, password)) {
            HttpCookie cookie = createCookie(user);
            return Response.builder()
                    .httpVersion(request.getStartLine().getVersion())
                    .httpStatus(HttpStatus.FOUND)
                    .setCookie(cookie)
                    .location(LOGIN_SUCCESS)
                    .build();
        }

        return Response.builder()
                .httpVersion(request.getStartLine().getVersion())
                .httpStatus(HttpStatus.FOUND)
                .location(LOGIN_FAIL)
                .build();
    }

    private static HttpCookie createCookie(User user) {
        Session session = sessionManager.newSession();
        session.setAttribute("user", user);

        return HttpCookie.of(
                new StringPair(JSESSIONID, session.getId()),
                new StringPair(PATH, ROOT)
        );
    }

    private static boolean authenticateUser(User user, String password) {
        return !Objects.isNull(user) && user.checkPassword(password);
    }
}
