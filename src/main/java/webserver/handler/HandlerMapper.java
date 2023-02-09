package webserver.handler;

import webserver.controller.Controller;
import webserver.controller.HelloController;
import webserver.controller.ResourceController;
import webserver.controller.UserCreateController;
import webserver.request.HttpRequest;

import java.util.List;

public class HandlerMapper {

    private static final List<Controller> controllers = List.of(
            new HelloController(),
            new ResourceController(),
            new UserCreateController()
    );

    public Controller getController(HttpRequest request) {
        return controllers.stream()
                .filter(controller -> controller.isMatch(request))
                .findFirst()
                .orElseThrow();
    }
}
