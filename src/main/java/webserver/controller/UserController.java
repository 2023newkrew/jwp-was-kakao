package webserver.controller;

import db.DataBase;
import http.HttpResponse;
import http.request.HttpRequest;
import http.request.RequestParam;
import model.User;

public class UserController {


    public void create(HttpRequest request, HttpResponse response) {
        RequestParam requestParam = request.getRequestParam();
        User user = new User(
                requestParam.get("userId"),
                requestParam.get("password"),
                requestParam.get("name"),
                requestParam.get("email")
        );

        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}
