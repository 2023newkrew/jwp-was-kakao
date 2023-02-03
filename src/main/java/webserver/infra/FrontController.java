package webserver.infra;

import lombok.experimental.UtilityClass;
import model.HttpRequest;
import webserver.controller.ApiController;
import webserver.controller.UserController;
import webserver.controller.ViewController;

import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static webserver.infra.ControllerHandlerAdapter.*;

@UtilityClass
public class FrontController {
    private final Map<String, ApiController> handleControllerMap = new HashMap<>();

    static {
        handleControllerMap.put("/user/create", UserController.getInstance());
    }

    public void handleRequest(HttpRequest request, DataOutputStream dos) {
        try {
            ApiController apiController = handleControllerMap.getOrDefault(request.getUrl(), ViewController.getInstance());

            if (isViewController(apiController)) {
                ViewResolver.resolve(request, dos);
                return;
            }

            Method method = findMethodToExecute(request, apiController);
            method.invoke(apiController, request, dos);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isViewController(ApiController apiController) {
        return apiController.getClass().isAssignableFrom(ViewController.class);
    }
}
