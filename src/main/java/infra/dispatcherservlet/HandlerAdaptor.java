package infra.dispatcherservlet;

import infra.controller.MyController;
import infra.controller.MyGetMapping;
import infra.controller.MyPostMapping;
import infra.utils.Api;
import infra.utils.request.MyRequest;
import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HandlerAdaptor {

    private static final Map<Api, MethodAndController> apiMethodMap = new HashMap<>();

    public HandlerAdaptor(MyController... controllers) {
        Arrays.stream(controllers)
                .forEach((controller) -> {
                    Method[] methods = controller.getClass().getDeclaredMethods();
                    Arrays.stream(methods)
                            .filter(method -> method.isAnnotationPresent(MyGetMapping.class))
                            .forEach(method -> {
                                MyGetMapping annotation = method.getAnnotation(MyGetMapping.class);
                                Arrays.stream(annotation.paths())
                                        .forEach((apiPath) -> apiMethodMap.put(new Api(apiPath, annotation.method()),
                                                new MethodAndController(method, controller)));
                            });

                });

        Arrays.stream(controllers)
                .forEach((controller) -> {
                    Method[] methods = controller.getClass().getDeclaredMethods();
                    Arrays.stream(methods)
                            .filter(method -> method.isAnnotationPresent(MyPostMapping.class))
                            .forEach(method -> {
                                MyPostMapping annotation = method.getAnnotation(MyPostMapping.class);
                                Arrays.stream(annotation.paths())
                                        .forEach((apiPath) -> apiMethodMap.put(new Api(apiPath, annotation.method()),
                                                new MethodAndController(method, controller)));
                            });

                });
    }

    public boolean handle(MyRequest myRequest, DataOutputStream dataOutputStream)
            throws InvocationTargetException, IllegalAccessException {
        MethodAndController methodAndController = apiMethodMap.get(myRequest.getApi());
        if (Objects.nonNull(methodAndController)) {
            methodAndController.getMethod()
                    .invoke(methodAndController.getMyController(), myRequest, dataOutputStream);
            return true;
        }
        return false;
    }


}
