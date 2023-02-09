package web.controller;

import http.HttpRequest;

import java.util.List;

public class Controllers {

    private static List<Controller> controllers;

    static {
        controllers = List.of(
                new DefaultController(),
                new GetResourceController(),
                new PostSignInController()
        );
    }

    public Controller getController(HttpRequest httpRequest) {
        return controllers.stream()
                .filter(controller -> controller.isMatch(httpRequest))
                .findAny()
                .orElseThrow(() -> new RuntimeException());
    }

}
