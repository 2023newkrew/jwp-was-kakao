package controller;

import controller.annotation.CustomRequestMapping;
import model.http.CustomHttpRequest;

import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class BaseController {

    protected Method getProperMethod(CustomHttpRequest request) throws NoSuchMethodException {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(CustomRequestMapping.class)
                        && method.getDeclaredAnnotation(CustomRequestMapping.class).url().equals(request.getHttpRequestLine().getUrl())
                        && method.getDeclaredAnnotation(CustomRequestMapping.class).httpMethod().equals(request.getHttpRequestLine().getHttpMethod())
                ).findFirst().orElseThrow(NoSuchMethodException::new);
    }
    
}
