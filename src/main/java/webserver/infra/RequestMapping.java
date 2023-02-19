package webserver.infra;

import model.annotation.Api;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestMapping {
    private static final Map<Object, Class<?>> requestMappingMap = new HashMap<>();

    public static Optional<Class<?>> get(Object key) {
        return Optional.ofNullable(requestMappingMap.getOrDefault(key, null));
    }

    public static void setAttributeFrom(Method method) {
        requestMappingMap.put(
                method.getDeclaredAnnotation(Api.class).url(),
                method.getDeclaringClass());
    }
}
