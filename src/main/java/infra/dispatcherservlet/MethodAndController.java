package infra.dispatcherservlet;

import infra.controller.MyController;
import java.lang.reflect.Method;

public class MethodAndController {

    private final Method method;
    private final MyController myController;

    public MethodAndController(Method method, MyController myController) {
        this.method = method;
        this.myController = myController;
    }

    public Method getMethod() {
        return method;
    }

    public MyController getMyController() {
        return myController;
    }
}
