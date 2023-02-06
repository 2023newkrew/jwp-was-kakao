package controller;

import enums.ContentType;
import org.springframework.http.HttpStatus;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class HomeController {

    private static HomeController instance;

    public static HomeController getInstance() {
        if (instance == null) {
            return new HomeController();
        }
        return instance;
    }

    private HomeController() {}

    public HttpResponse rootPathGet(HttpRequest request) {
        return HttpResponse.of(HttpStatus.OK, ContentType.HTML, "Hello world".getBytes());
    }
}
