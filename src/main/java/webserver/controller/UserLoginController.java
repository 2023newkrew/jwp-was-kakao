package webserver.controller;

import model.User;
import exception.LoginFailException;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.service.UserService;
import webserver.session.HttpSession;

public class UserLoginController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        try {
            User user = UserService.login(httpRequest);
            HttpSession httpSession = httpRequest.getHttpSession();
            httpSession.setAttribute("user", user);
            return HttpResponse.redirect(httpRequest, "http://localhost:8080/index.html");
        } catch (LoginFailException e) {
            return HttpResponse.redirect(httpRequest, "http://localhost:8080/user/login_failed.html");
        }

    }
}
