package webserver.controller;

import webserver.controller.annotation.Handler;
import webserver.controller.support.HandlerMethod;
import webserver.controller.support.PathMapKey;
import webserver.controller.annotation.RequestController;
import webserver.http.request.Request;

import java.lang.reflect.Method;
import java.util.*;

public class FrontController {
    private static final List<Object> controllers = new ArrayList<>();
    private static final Map<PathMapKey, HandlerMethod> handlerMap = new HashMap<>();

    static {
        addController(new ResourceController());
        addController(new HomeController());
        addController(new UserController());

        for (Object controller : controllers) {
            mapHandlerMethod(controller, controller.getClass().getMethods());
        }
    }

    private static void addController(Object controller) {
        if(!controller.getClass().isAnnotationPresent(RequestController.class)) {
            return;
        }
        controllers.add(controller);
    }

    private static void mapHandlerMethod(Object controller, Method[] methods) {
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Handler.class))
                .forEach(method -> {
                    Handler handler = method.getAnnotation(Handler.class);
                    handlerMap.put(PathMapKey.of(handler.method(), handler.value()), new HandlerMethod(method, controller));
                });
    }

    public static HandlerMethod getMappedMethod(Request request) {
        HandlerMethod method = handlerMap.get(PathMapKey.of(request.getHttpMethod(), request.getPath()));
        if (method == null) {
            method = handlerMap.get(PathMapKey.RESOURCE_KEY());
        }
        return method;
    }
}
