package webserver.infra;

import lombok.experimental.UtilityClass;
import model.request.HttpRequest;
import model.annotation.Api;
import webserver.controller.ApiController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class ControllerHandlerAdapter {
    public Method findMethodToExecute(HttpRequest request, ApiController apiController) {
        return Arrays.stream(apiController
                        .getClass()
                        .getDeclaredMethods()
                )
                .filter(method -> Optional.ofNullable(method.getDeclaredAnnotation(Api.class)).isPresent() &&
                        isMethodExistMatched(request, method.getDeclaredAnnotation(Api.class)))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private boolean isMethodExistMatched(HttpRequest request, Api annotation) {
        return request.methodAndURLEquals(annotation);
    }
}
