package controller;

import annotation.RequestMap;
import db.DataBase;
import model.User;
import request.Request;
import response.ContentType;
import response.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MainController implements Controller {

    public static final MainController INSTANCE = new MainController();

    private final Map<String, Method> map = new HashMap<>();

    private MainController() {
        List<Method> methodList = List.of(this.getClass().getMethods());
        for (Method method: methodList) {
            RequestMap requestMap = method.getAnnotation(RequestMap.class);
            if (requestMap != null) {
                map.put(requestMap.uri(), method);
            }
        }
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            return (Response) map.get(request.getUri()).invoke(MainController.INSTANCE, request);
        } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @RequestMap(uri = "/")
    public Response handleRootPage(Request request) {
        return Response.ok().contentType(ContentType.HTML).body("Hello world").build();
    }

    @RequestMap(uri = "/user/create")
    public Response handleUserCreate(Request request) {
        User user = User.from(request.getRequestParams());
        DataBase.addUser(user);

        return Response.found().location("/index.html").build();
    }
}
