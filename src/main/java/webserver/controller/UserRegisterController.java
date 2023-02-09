package webserver.controller;

import utils.ExistUserException;
import webserver.service.UserService;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class UserRegisterController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        try {
            UserService.registerUser(httpRequest);
            return HttpResponse.redirect(httpRequest, "http://localhost:8080/index.html");
        } catch (ExistUserException e) {
            return HttpResponse.redirect(httpRequest, "http://localhost:8080/user/register_failed.html");
        }
    }
}
