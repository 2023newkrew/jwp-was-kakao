package webserver.infra;

import lombok.experimental.UtilityClass;
import model.annotation.Api;
import webserver.controller.ApiController;
import model.HttpRequest;

import java.lang.reflect.Method;
import java.util.Optional;

@UtilityClass
public class ControllerHandlerAdapter {
    public Method findMethodToExecute(HttpRequest request, ApiController apiController) {
        Method[] methods = apiController.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Optional<Api> annotation = Optional.ofNullable(method.getDeclaredAnnotation(Api.class));

            if (annotation.isEmpty()) {
                continue;
            }

            if (isMethodExistMatched(request, annotation.get())) {
                return method;
            }
        }
        throw new RuntimeException();
    }

    private boolean isMethodExistMatched(HttpRequest request, Api annotation) {
        return annotation.method().equals(request.getHttpMethod()) && annotation.url().equals(request.getUrl());
    }
}
