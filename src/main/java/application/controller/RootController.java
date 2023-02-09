package application.controller;

import org.springframework.http.HttpStatus;
import webserver.handler.controller.AbstractController;
import webserver.handler.resolver.Resolver;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseBody;

public class RootController extends AbstractController {

    public RootController(Resolver viewResolver) {
        super(viewResolver);
        addGetHandler("/", this::index);
    }

    private Response index(Request request) {
        ResponseBody body = resolve("/index.html");

        return createResponse(HttpStatus.OK, null, body);
    }
}
