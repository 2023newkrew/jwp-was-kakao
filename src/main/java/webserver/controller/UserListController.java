package webserver.controller;

import db.DataBase;
import http.HttpSession;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;

import java.util.Collection;

public class UserListController extends GetController {
    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpSession session = httpRequest.getHttpSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            httpResponse.sendRedirect("/user/login.html");
            return;
        }

        Collection<User> users = DataBase.findAll();
        httpResponse.forward("user/list", users);
    }
}
