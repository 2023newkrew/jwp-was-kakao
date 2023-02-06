package controller;

import db.DataBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.user.User;
import utils.IOUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserController {


    private static UserController instance;

    public static UserController getInstance() {
        if (instance == null) {
            return new UserController();
        }
        return instance;
    }

    public HttpResponse createUserGet(HttpRequest request) {
        String requestPath = request.getRequestPath();
        Map<String, String> userInfo = IOUtils.extractUserFromPath(requestPath);
        if (userInfo.isEmpty()) {
        }
        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
        DataBase.addUser(user);

        return HttpResponse.create302FoundResponse("http://localhost:8080/index.html");
    }

    public HttpResponse createUserPost(HttpRequest request) {
        String requestBody = request.getBody();
        Map<String, String> userInfo = IOUtils.extractUser(requestBody);
        if (userInfo.isEmpty()) {
        }
        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
        DataBase.addUser(user);

        return HttpResponse.create302FoundResponse("http://localhost:8080/index.html");
    }
}
