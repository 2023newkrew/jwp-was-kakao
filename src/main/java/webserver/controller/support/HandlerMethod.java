package webserver.controller.support;

import webserver.http.request.Request;
import webserver.http.response.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {
    private Method method;
    private Object obj;

    public HandlerMethod(Method method, Object obj) {
        this.method = method;
        this.obj = obj;
    }

    public void handle(Request request, Response response) {
        try {
            method.invoke(obj, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
