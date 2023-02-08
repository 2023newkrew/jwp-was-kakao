package application.controller;

import application.service.UserService;
import org.springframework.http.HttpStatus;
import webserver.template.TemplateEngine;
import webserver.utils.IOUtils;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.util.Map;
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
        userService.save(userInfo);

        return HttpResponse
                .status(HttpStatus.FOUND)
                .location("http://localhost:8080/index.html")
                .build();
    }

    public HttpResponse createUserPost(HttpRequest request) {
        String requestBody = request.getBody();
        Map<String, String> userInfo = IOUtils.extract(requestBody);
        userService.save(userInfo);

        return HttpResponse
                .status(HttpStatus.FOUND)
                .location("http://localhost:8080/index.html")
                .build();
    }

    public HttpResponse userLogin(HttpRequest request) {
        String requestBody = request.getBody();
        Map<String, String> userInfo = IOUtils.extract(requestBody);

        if (userService.login(userInfo)) {
            return HttpResponse
                    .status(HttpStatus.FOUND)
                    .location("http://localhost:8080/index.html")
                    .setCookie("JSESSIONID", UUID.randomUUID().toString())
                    .setCookie("Path", "/")
                    .build();
        }

        return HttpResponse
                .status(HttpStatus.FOUND)
                .location("http://localhost:8080/user/login_failed.html")
                .build();
    }

    public HttpResponse userList(HttpRequest request) {
        return TemplateEngine.getTemplateResponse("/user/list", userService.findAllUsers());
    }
}
