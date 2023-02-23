package webserver.handler;

import webserver.controller.*;
import webserver.request.HttpRequest;

import java.util.List;

public class HandlerMapper {

    private static final List<Controller> controllers = List.of(
            new UserListController(),
            new HelloController(),
            new ResourceController(),
            new UserCreateController(),
            new LoginController()
    );

    public Controller getController(HttpRequest request) {
        return controllers.stream()
                .filter(controller -> controller.isMatch(request))
                .findFirst()
                .orElseThrow(() -> new NoSuchControllerException("컨트롤러가 존재하지 않습니다."));
    }
}
