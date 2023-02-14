package controller;

import utils.HttpResponseUtils;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class UserRegisterPageController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        return HttpResponseUtils.responseTemplatePage(httpRequest, "/user/form", false);
    }
}
