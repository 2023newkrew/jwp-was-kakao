package webserver.controller;

import db.DataBase;
import http.HttpResponse;
import http.request.HttpRequest;
import http.request.RequestParam;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public void create(HttpRequest request, HttpResponse response) {
        RequestParam requestParam = request.getRequestBodyParam();
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
