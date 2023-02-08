package webserver.controller;

import utils.LoginFailException;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.service.UserService;

public class UserLoginController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        try {
            UserService.login(httpRequest);
            return HttpResponse.redirect("/index.html");
        } catch (LoginFailException e) {
            return HttpResponse.redirect("/user/login_failed.html");
        }
    }
}
