package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.*;
import controller.dto.LoginRequest;
import controller.dto.UserRequest;
import model.User;
import service.UserService;
import support.PathNotFoundException;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public class UserController implements Controller {
    private static final String HOME_PATH = "/index.html";
    private static final String LOGIN_PATH = "/user/login.html";
    private static final String LOGIN_FAILED_PATH = "/user/login_failed.html";
    private static final String SESSION_COOKIE = "JSESSIONID";

    private static Map<String, BiConsumer<HttpRequest, HttpResponse>> mapping = new HashMap<>();

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

        // 요청 url -> method 매핑
        mapping.put("/user/create", this::createUser);
        mapping.put("/user/login", this::loginUser);
        mapping.put("/user/list", (req, res) ->  { if (authenticated(req, res)) getUsers(req, res); });
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
            response.setHeader(HttpHeader.LOCATION, HOME_PATH);
            response.setHttpStatus(HttpStatus.FOUND);
        }
    }

    public void loginUser(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.POST)) {
            LoginRequest loginRequest = LoginRequest.from(request.getParameters());
            boolean success = userService.loginUser(loginRequest.getUserId(), loginRequest.getPassword());

            if (success && request.getCookie(SESSION_COOKIE).isEmpty()) {
                response.setHeader(HttpHeader.SET_COOKIE, SESSION_COOKIE+"="+UUID.randomUUID()+"; path= /;");
            }
            response.setHttpStatus(HttpStatus.FOUND);
            response.setHeader(HttpHeader.LOCATION,  success ? HOME_PATH : LOGIN_FAILED_PATH);
        }
    }

    public void getUsers(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)) {
            try {
                response.setHttpStatus(HttpStatus.OK);
                response.setBody(getUserListPage().getBytes());
            } catch (IOException e) {
                response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                response.setBody(e.getMessage().getBytes());
            }
        }
    }

    public boolean authenticated(HttpRequest request, HttpResponse response) {
        if (request.getCookie(SESSION_COOKIE).isPresent()) return true;

        response.setHttpStatus(HttpStatus.FOUND);
        response.setHeader(HttpHeader.LOCATION, LOGIN_PATH);
        return false;
    }

    public String getUserListPage() throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader("/templates", ".html");
        Handlebars handlebars = new Handlebars(loader);
        Template template = handlebars.compile("/user/list");

        List<User> users = userService.getUsers();
        return template.apply(Map.of("users", users));
    }
}
