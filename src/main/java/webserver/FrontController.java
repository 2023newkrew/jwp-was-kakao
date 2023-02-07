package webserver;

import http.HttpResponse;
import http.request.HttpRequest;
import webserver.controller.Controller;
import webserver.controller.HomeController;
import webserver.controller.StaticResourceController;
import webserver.controller.UserSaveController;

import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private final Map<String, Controller> handlerMappingMap = new HashMap<>();
    private final StaticResourceController staticResourceController = new StaticResourceController();

    public FrontController() {
        initHandlerMappingMap();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/", new HomeController());
        handlerMappingMap.put("/user/create", new UserSaveController());
    }

    public void service(HttpRequest request, HttpResponse response) {

        Controller handler = getHandler(request);
        String resourcePath = handler.process(request, response);
        response.applyPath(resourcePath);
    }

    private Controller getHandler(HttpRequest request) {
        String path = request.getPath();
        if (path.contains(".")) {
            return staticResourceController;
        }
        Controller handler = handlerMappingMap.get(path);
        if (handler == null) {
            throw new IllegalArgumentException();
        }
        return handler;
    }

}
