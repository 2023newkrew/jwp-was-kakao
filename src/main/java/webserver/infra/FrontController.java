package webserver.infra;

import lombok.experimental.UtilityClass;
import model.request.HttpRequest;
import model.response.HttpResponse;
import utils.builder.ResponseBuilder;
import webserver.controller.ApiController;
import webserver.controller.LoginController;
import webserver.controller.UserController;
import webserver.controller.ViewController;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static webserver.infra.ControllerHandlerAdapter.*;

@UtilityClass
public class FrontController {
    private final Map<String, ApiController> handleControllerMap = new HashMap<>();

    static {
        handleControllerMap.put("/user/create", UserController.getInstance());
        handleControllerMap.put("/user/login/success", LoginController.getInstance());
        handleControllerMap.put("/user/list.html", UserController.getInstance());
        handleControllerMap.put("/user/login", LoginController.getInstance());
        handleControllerMap.put("/user/login.html", LoginController.getInstance());
    }

    public HttpResponse handleRequest(HttpRequest request) {
        try {
            ApiController apiController = handleControllerMap.getOrDefault(request.getURL(), ViewController.getInstance());
            if (isViewController(apiController)) {
                return ViewResolver.resolve(request);
            }
            return (HttpResponse) findMethodToExecute(request, apiController).invoke(apiController, request);
        } catch (InvocationTargetException | IllegalAccessException | RuntimeException e) {
            return ResponseBuilder.notFound();
        }
    }

    private boolean isViewController(ApiController apiController) {
        return apiController.getClass()
                .isAssignableFrom(ViewController.class);
    }
}
