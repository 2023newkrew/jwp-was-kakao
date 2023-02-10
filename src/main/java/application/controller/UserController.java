package application.controller;

import application.db.DataBase;
import application.model.User;
import application.model.Users;
import org.springframework.http.HttpStatus;
import webserver.handler.controller.AbstractController;
import webserver.handler.resolver.Resolver;
import webserver.http.Cookie;
import webserver.http.header.HeaderType;
import webserver.http.header.Headers;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

import java.util.Objects;

public class UserController extends AbstractController {

    private static final String ROOT_PATH = "/";

    private static final String INDEX_PATH = "/index.html";

    private static final String LOGIN_PATH = "/user/login.html";

    private static final String LOGIN_FAILED_PATH = "/user/login_failed.html";
    private static final String LIST_PATH = "/user/list.html";

    public UserController(Resolver viewResolver) {
        super(viewResolver);
        addPostHandler("/user/create", this::createUser);
        addGetHandler("/user/login", this::loginPage);
        addPostHandler("/user/login", this::login);
        addGetHandler("/user/list", this::list);
    }

    private Response loginPage(Request request) {
        return createResponse(HttpStatus.OK, resolve(LOGIN_PATH));
    }

    private Response createUser(Request request) {
        DataBase.addUser(getUser(request));

        var headers = new Headers();
        headers.put(HeaderType.LOCATION, INDEX_PATH);

        return createResponse(HttpStatus.FOUND, headers);
    }

    private User getUser(Request request) {
        return new User(
                request.getBody("userId"),
                request.getBody("password"),
                request.getBody("name"),
                request.getBody("email")
        );
    }

    private Response login(Request request) {
        String userId = request.getBody("userId");
        String password = request.getBody("password");
        if (loginSuccess(userId, password)) {
            Session session = createSession();
            session.setUserId(userId);

            return createLoginResponse(session);
        }

        return createResponse(HttpStatus.OK, resolve(LOGIN_FAILED_PATH));
    }

    private boolean loginSuccess(String userId, String password) {
        try {
            User user = DataBase.findUserById(userId);

            return password.equals(user.getPassword());
        }
        catch (Exception ignore) {
            return false;
        }
    }

    private Response createLoginResponse(Session session) {
        var headers = new Headers();
        headers.put(HeaderType.LOCATION, ROOT_PATH);
        headers.put(HeaderType.SET_COOKIE, new Cookie(session.getId(), ROOT_PATH));

        return createResponse(HttpStatus.FOUND, headers);
    }

    private Response list(Request request) {
        if (loggedIn(request)) {
            var body = resolveTemplate(
                    LIST_PATH,
                    new Users(DataBase.findAll())
            );
            return createResponse(HttpStatus.OK, body);
        }

        return createResponse(HttpStatus.FOUND, resolve(LOGIN_PATH));
    }

    private boolean loggedIn(Request request) {
        try {
            Cookie cookie = request.getCookie();
            Session session = getSession(cookie.getJSessionId());

            return Objects.nonNull(session);
        }
        catch (Exception ignore) {
            return false;
        }
    }
}
