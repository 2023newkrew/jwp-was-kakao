package webserver.infra;

import lombok.experimental.UtilityClass;
import model.request.HttpRequest;
import model.response.HttpResponse;
import utils.builder.ResponseBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static webserver.infra.ControllerHandlerAdapter.*;

@UtilityClass
public class FrontController {
    private final String INSTANTIATE_METHOD_NAME = "getInstance";

    static {
        ControllerHandlerAdapter.mappingControllerByURL();
    }

    public HttpResponse handleRequest(HttpRequest request) {
        try {
            Optional<Class<?>> controller = RequestMapping.get(request.getURL());

            if (controller.isEmpty()) {
                return ViewResolver.resolve(request);
            }

            return (HttpResponse) findMethodToExecute(controller.get(), request)
                    .invoke(getInstance(controller.get()), request);
        } catch (Exception e) {
            return ResponseBuilder.notFound();
        }
    }

    private Object getInstance(Class<?> controller) throws Exception {
        return controller.getMethod(INSTANTIATE_METHOD_NAME)
                .invoke(controller);
    }
}
