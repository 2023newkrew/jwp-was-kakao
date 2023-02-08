package webserver.controller;

import utils.HttpRequestUtils;
import utils.LoginFailException;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.service.UserService;

public class UserLoginController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        try {
            UserService.login(httpRequest);
            return HttpResponse.redirect(httpRequest, "http://localhost:8080/index.html");
        } catch (LoginFailException e) {
            return HttpResponse.redirect(httpRequest, "http://localhost:8080/user/login_failed.html");
        }
    }
}
