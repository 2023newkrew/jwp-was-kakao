package web.controller;

import error.ApplicationException;
import http.request.HttpRequest;

import java.util.List;

import static error.ErrorType.CONTROLLER_NOT_FOUND;

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
                .orElseThrow(() -> new ApplicationException(CONTROLLER_NOT_FOUND));
    }

}
