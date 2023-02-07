package controller;

import model.user.UserService;
import org.springframework.http.HttpStatus;
import utils.IOUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

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
        String requestPath = request.getRequestPath();
        Map<String, String> userInfo = IOUtils.extractUserFromPath(requestPath);
        userService.save(userInfo);

        return HttpResponse
                .status(HttpStatus.FOUND)
                .location("http://localhost:8080/index.html")
                .build();
    }

    public HttpResponse createUserPost(HttpRequest request) {
        String requestBody = request.getBody();
        Map<String, String> userInfo = IOUtils.extractUser(requestBody);
        userService.save(userInfo);

        return HttpResponse
                .status(HttpStatus.FOUND)
                .location("http://localhost:8080/index.html")
                .build();
    }
}
