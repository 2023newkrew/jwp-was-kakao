package was.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controllers {
    private final Map<Class<?>, Object> controllerMap = new HashMap<>();

    public Controllers(List<Class<?>> classes) {
        classes.forEach(it -> controllerMap.put(it, getInstance(it)));
    }

    private Object getInstance(Class<?> classType) {
        try {
            return classType.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            throw new RuntimeException("생성자 문제");
        }
    }

    public Object getController(Class<?> controller) {
        return controllerMap.getOrDefault(controller, null);
    }
}
