package webserver.controller;

import db.DataBase;
import http.HttpSession;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLoginController extends PostController {
    static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        String userId = httpRequest.getParameter("userId");
        String password = httpRequest.getParameter("password");

        User user = DataBase.findUserById(userId);

        if (user != null && user.isValid(userId, password)) {
            HttpSession session = httpRequest.getHttpSession();
            session.setAttribute("user", user);
            httpResponse.sendRedirect("/index.html");
            return;
        }

        httpResponse.sendRedirect("/user/login_failed.html");
    }
}
