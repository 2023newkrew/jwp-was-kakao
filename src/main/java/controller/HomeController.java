package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public class HomeController {

    private static HomeController instance;

    private HomeController() {
    }

    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }

    public HttpResponse rootPathGet(HttpRequest request) {
        return HttpResponse.body("Hello world");
    }
}
