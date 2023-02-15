package controller;

import controller.annotation.CustomRequestBody;
import controller.annotation.CustomRequestHeader;
import controller.annotation.CustomRequestMapping;
import controller.annotation.CustomRequestParams;
import exception.UnsupportedRequestException;
import exception.UnsupportedResponseException;
import exception.UserNotFoundException;
import model.http.CustomBaseHttpRequest;
import model.http.CustomHttpRequest;
import model.http.CustomHttpResponse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
        controllerMapping.put("/user/login", new UserController());
        controllerMapping.put("/user/list", new UserController());
    }

    public static FrontController getInstance() {
        return frontController;
    }

    public CustomHttpResponse getHttpResponse(CustomHttpRequest request) throws NoSuchMethodException {
        BaseController controller = controllerMapping.getOrDefault(request.getHttpRequestLine().getUrl(), new ViewController());
        Method foundMethod = getProperMethod(controller, request);
        try {
            return getResponse(foundMethod, controller, request);
        } catch (Exception e) {
            throw new UnsupportedResponseException("HttpResponse 생성에 실패했습니다.");
        }
    }

    private Method getProperMethod(BaseController controller, CustomHttpRequest request) throws NoSuchMethodException {
        if (controller instanceof ViewController) {
            return Arrays.stream(controller.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(CustomRequestMapping.class)
                            && method.getDeclaredAnnotation(CustomRequestMapping.class).url().equals(request.getHttpRequestLine().getUrl())
                            && method.getDeclaredAnnotation(CustomRequestMapping.class).httpMethod().equals(request.getHttpRequestLine().getHttpMethod())
                    ).findFirst()
                    .orElse(controller.getClass().getDeclaredMethod("resource", CustomHttpRequest.class));
        }
        return Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(CustomRequestMapping.class)
                        && method.getDeclaredAnnotation(CustomRequestMapping.class).url().equals(request.getHttpRequestLine().getUrl())
                        && method.getDeclaredAnnotation(CustomRequestMapping.class).httpMethod().equals(request.getHttpRequestLine().getHttpMethod())
                ).findFirst().orElseThrow(NoSuchMethodException::new);
    }

    private CustomHttpResponse getResponse(Method method, BaseController controller, CustomHttpRequest request) throws InvocationTargetException, IllegalAccessException {
        if (method.getParameterCount() == 1 && method.getParameters()[0].isAnnotationPresent(CustomRequestBody.class)) {
            return getResponseByRequest(method, request.getBody(), controller);
        }
        if (method.getParameterCount() == 1 && method.getParameters()[0].isAnnotationPresent(CustomRequestParams.class)) {
            return getResponseByRequest(method, request.getHttpRequestLine().getQuery(), controller);
        }
        if (method.getParameterCount() == 1 && method.getParameters()[0].isAnnotationPresent(CustomRequestHeader.class)) {
            return (CustomHttpResponse) method.invoke(controller, request.getHeaders());
        }
        if (method.getName().equals("resource")) {
            return (CustomHttpResponse) method.invoke(controller, request);
        }
        return (CustomHttpResponse) method.invoke(controller);
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
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new UnsupportedRequestException("HttpRequest 매핑에 실패했습니다.");
        }
    }

}
