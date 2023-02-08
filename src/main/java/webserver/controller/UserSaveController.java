package webserver.controller;

import db.DataBase;
import http.HttpResponse;
import http.ResponseStatus;
import http.request.HttpRequest;
import http.request.RequestParam;
import model.User;

public class UserSaveController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        RequestParam requestParam = request.getRequestParam();

        User user = new User(
                requestParam.get("userId"),
                requestParam.get("password"),
                requestParam.get("name"),
                requestParam.get("email")
        );
        DataBase.addUser(user);

        response.setResponseStatus(ResponseStatus.FOUND);
        return "/index.html";
    }
}
