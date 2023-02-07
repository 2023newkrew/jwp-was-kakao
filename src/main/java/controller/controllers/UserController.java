package controller.controllers;

import controller.annotation.CustomRequestMapping;
import db.DataBase;
import http.CustomHttpMethod;
import http.CustomHttpRequest;
import http.CustomHttpResponse;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class UserController extends BaseController {

    @CustomRequestMapping(url = "/user/create", httpMethod = CustomHttpMethod.POST)
    public CustomHttpResponse create(CustomHttpRequest request) {
        User user = new User(
                request.getQuery().get("userId"),
                request.getQuery().get("password"),
                request.getQuery().get("name"),
                request.getQuery().get("email")
        );
        DataBase.addUser(user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Location", "/index.html");
        return new CustomHttpResponse("HTTP/1.1 302 FOUND", headers, "");
    }

}
