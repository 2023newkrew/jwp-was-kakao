package application.controller;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

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
