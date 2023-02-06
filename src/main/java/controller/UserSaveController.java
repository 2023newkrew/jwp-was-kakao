package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;

public class UserSaveController implements PostMethodController {
    @Override
    public String process(HttpRequest httpRequest) {
        DataBase.addUser(new User(
                httpRequest.getParameter("userId"),
                httpRequest.getParameter("password"),
                httpRequest.getParameter("name"),
                httpRequest.getParameter("email")
        ));

        return "/index.html";
    }
}
