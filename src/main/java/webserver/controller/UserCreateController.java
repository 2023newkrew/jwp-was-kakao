package webserver.controller;

import db.DataBase;
import http.request.HttpRequest;
import http.request.RequestParam;
import http.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCreateController extends PostController {
    static final Logger logger = LoggerFactory.getLogger(UserCreateController.class);

    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        RequestParam requestParam = httpRequest.getRequestBodyParam();
        User user = new User(
                requestParam.get("userId"),
                requestParam.get("password"),
                requestParam.get("name"),
                requestParam.get("email")
        );
        DataBase.addUser(user);
        httpResponse.sendRedirect("/index.html");
    }
}
