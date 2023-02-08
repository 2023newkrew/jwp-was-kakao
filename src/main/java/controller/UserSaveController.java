package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

public class UserSaveController implements PostMethodController {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        DataBase.addUser(new User(
                httpRequest.getParameter("userId"),
                httpRequest.getParameter("password"),
                httpRequest.getParameter("name"),
                httpRequest.getParameter("email")
        ));

        httpResponse.sendRedirect("/index.html");
        throw new BreakException();
    }
}
