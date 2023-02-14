package webserver.controller;

import lombok.RequiredArgsConstructor;
import webserver.model.request.Request;
import webserver.model.response.Response;
import webserver.model.response.StatusCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class RequestMappingHandler {
    private final GlobalController controller;
    private static final String BASE_URL = "";

    public Response handle(Request request) throws InvocationTargetException, IllegalAccessException {

        Method[] methods = controller.getClass().getMethods();
        Optional<Method> invokeMethod = Arrays.stream(methods).filter(method ->
                        method.isAnnotationPresent(CustomRequestMapping.class) &&
                                method.getAnnotation(CustomRequestMapping.class).method() == request.getMethod() &&
                                method.getAnnotation(CustomRequestMapping.class).path().equals(BASE_URL + request.getPath()))
                .findAny();

        if (invokeMethod.isEmpty()) {
            return Response.of(StatusCode.NOT_FOUND);
        }
        return (Response) invokeMethod.get().invoke(controller, request);
    }
}
