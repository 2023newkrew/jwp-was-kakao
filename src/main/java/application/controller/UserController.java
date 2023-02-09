package application.controller;

import application.db.DataBase;
import application.model.User;
import org.springframework.http.HttpStatus;
import webserver.handler.controller.AbstractController;
import webserver.handler.resolver.Resolver;
import webserver.http.Cookie;
import webserver.http.header.HeaderType;
import webserver.http.header.Headers;
import webserver.request.Request;
import webserver.response.Response;

import java.util.UUID;

public class UserController extends AbstractController {

    private static final String ROOT_PATH = "/";

    private static final String INDEX_PATH = "/index.html";

    private static final String LOGIN_PATH = "/user/login.html";

    private static final String LOGIN_FAILED_PATH = "/user/login_failed.html";

    public UserController(Resolver viewResolver) {
        super(viewResolver);
        addPostHandler("/user/create", this::createUser);
        addGetHandler("/user/login", this::loginPage);
        addPostHandler("/user/login", this::login);
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

    public Response login(Request request) {
        var headers = new Headers();
        if (loginSuccess(request)) {
            headers.put(HeaderType.LOCATION, ROOT_PATH);
            headers.put(HeaderType.SET_COOKIE, new Cookie(UUID.randomUUID(), ROOT_PATH));

            return createResponse(HttpStatus.FOUND, headers);
        }

        return createResponse(HttpStatus.OK, headers, resolve(LOGIN_FAILED_PATH));
    }

    private static boolean loginSuccess(Request request) {
        try {
            String userId = request.getBody("userId");
            String password = request.getBody("password");
            User user = DataBase.findUserById(userId);

            return password.equals(user.getPassword());
        }
        catch (Exception ignore) {
            return false;
        }
    }

}
