package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class UserSaveController implements PostMethodController {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException {
        DataBase.addUser(new User(
                httpRequest.getParameter("userId"),
                httpRequest.getParameter("password"),
                httpRequest.getParameter("name"),
                httpRequest.getParameter("email")
        ));

        String location = "/index.html";
        httpResponse.sendRedirect(location);
        throw new RedirectException(location);
    }
}
