package controller;

import controller.annotation.CustomRequestBody;
import controller.annotation.CustomRequestParams;
import exception.UnsupportedRequestException;
import exception.UnsupportedResponseException;
import model.http.CustomBaseHttpRequest;
import model.http.CustomHttpRequest;
import model.http.CustomHttpResponse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FrontController {

    private final static FrontController frontController = new FrontController();
    private final Map<String, BaseController> controllerMapping = new HashMap<>();

    private FrontController() {
        controllerMapping.put("/user/create", new UserController());
    }

    public static FrontController getInstance() {
        return frontController;
    }

    public CustomHttpResponse getHttpResponse(CustomHttpRequest request) throws NoSuchMethodException {
        BaseController controller = controllerMapping.getOrDefault(request.getUrl(), new ViewController());
        Method foundMethod = controller.getProperMethod(request);
        CustomHttpResponse response;
        try {
            if (foundMethod.getParameterCount() == 1 && foundMethod.getParameters()[0].isAnnotationPresent(CustomRequestBody.class)) {
                response = getResponseByRequest(foundMethod, request.getBody(), controller);
            } else if (foundMethod.getParameterCount() == 1 && foundMethod.getParameters()[0].isAnnotationPresent(CustomRequestParams.class)) {
                response = getResponseByRequest(foundMethod, request.getQuery(), controller);
            } else if (foundMethod.getName().equals("resource")) {
                response = (CustomHttpResponse) foundMethod.invoke(controller, request);
            } else {
                response = (CustomHttpResponse) foundMethod.invoke(controller);
            }
        } catch (Exception e) {
            throw new UnsupportedResponseException("HttpResponse 생성에 실패했습니다.");
        }
        return response;
    }

    private CustomHttpResponse getResponseByRequest(Method method, CustomBaseHttpRequest request, BaseController controller) {
        try {
            Class target = method.getParameters()[0].getType();
            Constructor constructor = target.getDeclaredConstructor(
                    Arrays.stream(target.getDeclaredFields())
                            .map(f -> f.getType())
                            .collect(Collectors.toList())
                            .toArray(new Class[0])
            );
            Field[] fields = target.getDeclaredFields();
            return (CustomHttpResponse) method.invoke(
                    controller,
                    constructor.newInstance(Arrays.stream(fields)
                            .map(f -> request.get(f.getName()))
                            .collect(Collectors.toList())
                            .toArray())
            );
        } catch (Exception e) {
            throw new UnsupportedRequestException("HttpRequest 매핑에 실패했습니다.");
        }
    }

}
