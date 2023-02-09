package webserver.infra;

import lombok.experimental.UtilityClass;
import model.annotation.Api;
import model.annotation.ApiController;
import model.request.HttpRequest;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class ControllerHandlerAdapter {
    private final String CONTROLLER_PACKAGE = "webserver.controller";
    public void mappingControllerByURL() {
        new Reflections(CONTROLLER_PACKAGE).getTypesAnnotatedWith(ApiController.class)
                .stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(method -> method.isAnnotationPresent(Api.class))
                .forEach(RequestMapping::setAttributeFrom);
    }

    public Method findMethodToExecute(Class<?> controller, HttpRequest request) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> Optional.ofNullable(method.getDeclaredAnnotation(Api.class)).isPresent() &&
                        isMethodExistMatched(request, method.getDeclaredAnnotation(Api.class)))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private boolean isMethodExistMatched(HttpRequest request, Api annotation) {
        return request.methodAndURLAndContentEquals(annotation);
    }
}
