package learn;

import org.junit.jupiter.api.Test;
import webserver.Api;
import webserver.FrontController;
import webserver.UserController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class IteratorClassLearn {
    @Test
    public void getMethodsInClass() throws NoSuchFieldException {
        UserController userController = UserController.getInstance();
        Method[] methods = userController.getClass().getMethods();
        for (Method method : methods) {
            Api[] annotations = method.getDeclaredAnnotationsByType(Api.class);
            for (Api annotation : annotations) {
                System.out.println("annotation.method() = " + annotation.method());
            }
        }
    }
}
