package controller;

import exception.ExistUserException;
import utils.HttpResponseUtils;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import service.UserService;

public class UserRegisterController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        try {
            UserService.registerUser(httpRequest);
            return HttpResponse.redirect(httpRequest, "http://localhost:8080/index.html");
        } catch (ExistUserException e) {
            return HttpResponseUtils.responseTemplatePage(httpRequest, "/user/form", true);
        }
    }
}
