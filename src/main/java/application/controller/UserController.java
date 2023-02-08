package application.controller;

import application.dto.UserResponse;
import application.service.UserService;
import application.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import webserver.template.TemplateEngine;
import webserver.utils.IOUtils;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserController {

    private static UserController instance;
    private final UserService userService;

    private UserController() {
        this.userService = UserService.getInstance();
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public HttpResponse createUserGet(HttpRequest request) {
        String requestPath = request.getRequestURL();
        Map<String, String> userInfo = IOUtils.extractQueryParameterInfo(requestPath);

        UserResponse user = userService.save(userInfo);
        SecurityUtils.setUserToSession(request, user);

        return HttpResponse
                .status(HttpStatus.FOUND)
                .location("http://localhost:8080/index.html")
                .build();
    }

    public HttpResponse createUserPost(HttpRequest request) {
        String requestBody = request.getBody();
        Map<String, String> userInfo = IOUtils.extract(requestBody);

        UserResponse user = userService.save(userInfo);
        SecurityUtils.setUserToSession(request, user);

        return HttpResponse
                .status(HttpStatus.FOUND)
                .location("http://localhost:8080/index.html")
                .build();
    }

    public HttpResponse userLogin(HttpRequest request) {
        String requestBody = request.getBody();
        Map<String, String> userInfo = IOUtils.extract(requestBody);

        Optional<UserResponse> userOpt = userService.login(userInfo);

        if (userOpt.isPresent()) {
            SecurityUtils.setUserToSession(request, userOpt.get());

            return HttpResponse
                    .status(HttpStatus.FOUND)
                    .location("http://localhost:8080/index.html")
                    .setCookie("JSESSIONID", request.getSession().getId())
                    .setCookie("Path", "/")
                    .build();
        }

        return HttpResponse
                .status(HttpStatus.FOUND)
                .location("http://localhost:8080/user/login_failed.html")
                .build();
    }

    public HttpResponse userList(HttpRequest request) {
        if (SecurityUtils.isLoggedIn(request)) {
            return TemplateEngine.getTemplateResponse("/user/list", userService.findAllUsers());
        }
        return HttpResponse
                .status(HttpStatus.FOUND)
                .location("http://localhost:8080/user/login.html")
                .build();
    }
}
