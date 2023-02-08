package webserver.controller;

import webserver.service.UserService;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class UserCreateController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        UserService.createUser(httpRequest);

        return HttpResponse.redirect("http://localhost:8080/index.html");
    }
}
