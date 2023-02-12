package webserver;

import webserver.handler.Handler;
import webserver.handler.LoginHandler;
import webserver.handler.UserHandler;
import webserver.handler.UserListHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerMapping {
    private static final Map<String, Handler> HANDLERS = new ConcurrentHashMap<>();

    static {
        HANDLERS.put("/user/create", new UserHandler());
        HANDLERS.put("/user/list", new UserListHandler());
        HANDLERS.put("/user/login", new LoginHandler());
    }

    public Handler getHandler(String uri) {
        return HANDLERS.get(uri);
    }
}
