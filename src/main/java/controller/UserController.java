package controller;

import common.*;
import controller.dto.LoginRequest;
import controller.dto.UserRequest;
import service.UserService;
import support.PathNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

public class UserController implements Controller {
    private static final String SUCCESS_REDIRECT_PATH = "/index.html";
    private static final String LOGIN_FAIL_REDIRECT_PATH = "/user/login_failed.html";
    private static final String SESSION_COOKIE = "JSESSIONID";

    private static Map<String, BiConsumer<HttpRequest, HttpResponse>> mapping = new HashMap<>();

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

        // 요청 url -> method 매핑
        mapping.put("/user/create", this::createUser);
        mapping.put("/user/login", this::loginUser);
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
            response.setHeader(HttpHeader.LOCATION, SUCCESS_REDIRECT_PATH);
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
            response.setHeader(HttpHeader.LOCATION,  success ? SUCCESS_REDIRECT_PATH : LOGIN_FAIL_REDIRECT_PATH);
        }
    }
}
