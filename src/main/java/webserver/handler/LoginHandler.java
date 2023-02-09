package webserver.handler;

import db.DataBase;
import model.User;
import service.UserService;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.session.Session;
import webserver.http.session.SessionManager;

import java.util.Optional;

public class LoginHandler extends AbstractHandler {

    @Override
    protected void doGet(final HttpRequest request, final HttpResponse response) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(final HttpRequest request, final HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        boolean isAuthenticated = new UserService().login(userId, password);
        if (isAuthenticated) {
            String jsessionId = request.getCookie("JSESSIONID").getValue();
            Session session = SessionManager.findSession(jsessionId);
            Optional<User> user = DataBase.findByUserId(userId);
            user.ifPresent(value -> session.setAttribute("user", value));
            response.setHeader("Location", "/index.html");
            response.setHeader("Set-Cookie", "logined=true");
        } else {
            response.setHeader("Location", "/user/login_failed.html");
        }
        response.setStatus(HttpStatus.FOUND);
    }
}
