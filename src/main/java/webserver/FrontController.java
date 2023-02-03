package webserver;

import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static webserver.HandlerControllerAdapter.*;

public class FrontController {
    private static final Map<String, ApiController> handleControllerMap = new HashMap<>();

    static {
        handleControllerMap.put("/user/create", UserController.getInstance());
    }

    public static void handleRequest(HttpRequest request, DataOutputStream dos) {
        try {
            ApiController apiController = handleControllerMap.get(request.getUrl());
            Method method = findMethodToExecute(request, apiController);

            method.invoke(apiController, request, dos);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
