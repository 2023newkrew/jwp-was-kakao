package webserver;

import webserver.handler.Handler;
import webserver.handler.LoginHandler;
import webserver.handler.UserHandler;
import webserver.handler.UserListHandler;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private static final Map<String, Handler> map = new HashMap<>();

    static {
        map.put("/user/create", new UserHandler());
        map.put("/user/list", new UserListHandler());
        map.put("/user/login", new LoginHandler());
    }

    public Handler getHandler(String uri) {
        return map.get(uri);
    }
}
