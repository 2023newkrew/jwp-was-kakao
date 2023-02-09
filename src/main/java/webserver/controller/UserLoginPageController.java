package webserver.controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class UserLoginPageController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        if (httpRequest.getHttpSession().getAttribute("user") != null) {
            return HttpResponse.redirect(httpRequest, "http://localhost:8080/index.html");
        }
        return new FileLoadController().response(httpRequest);
    }
}
