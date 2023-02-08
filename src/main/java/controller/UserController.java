package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.user.User;
import utils.IOUtils;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserController {
    public static final String CREATE_USER_REDIRECT_URI = "http://localhost:8080/index.html";
    private static UserController instance;

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public HttpResponse createUserGet(HttpRequest request) {
        String requestPath = request.getRequestPath();
        Map<String, String> userInfo = IOUtils.extractUserFromPath(requestPath);
        User user = User.from(userInfo);
        DataBase.addUser(user);

        return HttpResponse.create302FoundResponse(CREATE_USER_REDIRECT_URI);
    }

    public HttpResponse createUserPost(HttpRequest request) {
        String requestBody = request.getBody();
        Map<String, String> userInfo = IOUtils.extractUser(requestBody);
        User user = User.from(userInfo);
        DataBase.addUser(user);

        return HttpResponse.create302FoundResponse(CREATE_USER_REDIRECT_URI);
    }
}
