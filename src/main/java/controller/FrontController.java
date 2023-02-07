package controller;

import controller.annotation.CustomRequestMapping;
import controller.controllers.BaseController;
import controller.controllers.UserController;
import controller.controllers.ViewController;
import http.request.CustomHttpRequest;
import http.response.CustomHttpResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private final static FrontController frontController = new FrontController();
    private final Map<String, BaseController> controllerMapping = new HashMap<>();

    private FrontController() {
        controllerMapping.put("/user/create", new UserController());
    }

    public static FrontController getInstance() {
        return frontController;
    }

    public CustomHttpResponse getHttpResponse(CustomHttpRequest request) {
        BaseController controller = controllerMapping.getOrDefault(request.getUrl(), new ViewController());
        Method foundMethod = Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(CustomRequestMapping.class)
                        && method.getDeclaredAnnotation(CustomRequestMapping.class).url().equals(request.getUrl())
                        && method.getDeclaredAnnotation(CustomRequestMapping.class).httpMethod().equals(request.getHttpMethod())
                ).findFirst().orElseThrow(RuntimeException::new);

        CustomHttpResponse response;
        try {
            response = (CustomHttpResponse) foundMethod.invoke(controller, request);
        } catch (Exception e) {
            throw new IllegalArgumentException("HttpResponse 생성에 실패했습니다.");
        }
        return response;
    }

}
