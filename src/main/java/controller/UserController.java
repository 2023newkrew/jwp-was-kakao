package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.*;
import controller.dto.LoginRequest;
import controller.dto.UserRequest;
import model.User;
import service.AuthService;
import service.UserService;
import support.LoginFailedException;
import support.PathNotFoundException;
import support.UserNotFoundException;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public class UserController implements Controller {
    private static final String HOME_PATH = "/index.html";
    private static final String LOGIN_PATH = "/user/login.html";
    private static final String LOGIN_FAILED_PATH = "/user/login_failed.html";
    private static final Map<String, BiConsumer<HttpRequest, HttpResponse>> mapping = new HashMap<>();

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;

        // 요청 url -> method 매핑
        mapping.put("/user/create", this::createUser);
        mapping.put("/user/login", (req, res) -> routeWithAuth(req, res, this::loginUser, false));
        mapping.put("/user/list", (req, res) ->  routeWithAuth(req, res, this::getUserListPage, true));
    }

    public void routeWithAuth(HttpRequest request, HttpResponse response,
                              BiConsumer<HttpRequest, HttpResponse> method, boolean authenticated) {
        if (authenticated) {
            if (authService.isAuthenticated(request)) {
                method.accept(request, response);
            }
            else {
                setRedirectResponse(response, LOGIN_PATH);
            }
        }
        else if (!authenticated) {
            if (!authService.isAuthenticated(request)) {
                method.accept(request, response);
            }
            else {
                setRedirectResponse(response, HOME_PATH);
            }
        }
    }
    
    public void setRedirectResponse(HttpResponse response, String path) {
        response.setHttpStatus(HttpStatus.FOUND);
        response.setHeader(HttpHeader.LOCATION, path);
    }

    public void process(HttpRequest request, HttpResponse response) {
        if (Objects.isNull(mapping.get(request.getUri()))) {
            throw new PathNotFoundException();
        }
        mapping.get(request.getUri()).accept(request, response);
    }

    public void createUser(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET) || request.getMethod().equals(HttpMethod.POST)) {
            UserRequest userRequest = UserRequest.from(request.getParameters());
            userService.addUser(
                    userRequest.getUserId(),
                    userRequest.getPassword(),
                    userRequest.getName(),
                    userRequest.getEmail()
            );
            setRedirectResponse(response, HOME_PATH);
        }
    }

    public void loginUser(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.POST)) {
            LoginRequest loginRequest = LoginRequest.from(request.getParameters());
            try {
                User user = userService.getUser(loginRequest.getUserId());
                String sessionId = authService.login(user, loginRequest.getPassword());
                response.setHeader(HttpHeader.SET_COOKIE, AuthService.SESSION_COOKIE+"="+sessionId+"; path= /;");
                setRedirectResponse(response, HOME_PATH);
            } catch (UserNotFoundException | LoginFailedException e) {
                setRedirectResponse(response, LOGIN_FAILED_PATH);
            }
        }
    }

    public void getUserListPage(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)) {
            try {
                response.setHttpStatus(HttpStatus.OK);
                response.setBody(createUserListPage(userService.getUsers()).getBytes());
            } catch (IOException e) {
                response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                response.setBody(e.getMessage().getBytes());
            }
        }
    }

    private String createUserListPage(List<User> users) throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader("/templates", ".html");
        Handlebars handlebars = new Handlebars(loader);
        Template template = handlebars.compile("/user/list");
        return template.apply(Map.of("users", users));
    }
}
