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

public final class UserController implements Controller {

    private final Map<String, Method> map = new HashMap<>();

    private static class LazyHolder {
        public static final UserController INSTANCE = new UserController();
    }

    public static UserController getInstance() {
        return LazyHolder.INSTANCE;
    }

    private UserController() {
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
            return (Response) map.get(request.getUri()).invoke(UserController.getInstance(), request);
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
