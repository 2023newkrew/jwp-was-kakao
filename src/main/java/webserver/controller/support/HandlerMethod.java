package webserver.controller.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.http.request.Request;
import webserver.http.response.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
