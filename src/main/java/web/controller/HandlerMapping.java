package web.controller;

import error.ApplicationException;
import http.request.HttpRequest;

import java.util.List;

import static error.ErrorType.CONTROLLER_NOT_FOUND;

public class HandlerMapping {

    private final List<Controller> controllers;

    private HandlerMapping(List<Controller> controllers) {
        this.controllers = controllers;
    }

    public static HandlerMapping of(Controller... controllers) {
        return new HandlerMapping(List.of(controllers));
    }

    public Controller getHandler(HttpRequest httpRequest) {
        return controllers.stream()
                .filter(controller -> controller.isMatch(httpRequest))
                .findAny()
                .orElseThrow(() -> new ApplicationException(CONTROLLER_NOT_FOUND));
    }

}
