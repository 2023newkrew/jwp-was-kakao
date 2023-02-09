package webserver.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import webserver.controller.annotation.Handler;
import webserver.controller.support.RequestController;
import webserver.request.Request;
import webserver.response.Response;

@RequestController
public class HomeController {

    @Handler(method = HttpMethod.GET, value = "/")
    public void index(Request req, Response res) {
        byte[] body = "Hello world".getBytes();
        res.setBody(body);
        res.setStatus(HttpStatus.OK);
    }
}
