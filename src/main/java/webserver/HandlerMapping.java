package webserver;

import webserver.handler.Handler;
import webserver.handler.UserHandler;

import java.util.Map;

public class HandlerMapping {
    private final Map<String, Handler> map = Map.of("/user/create", new UserHandler());

    public Handler getHandler(String uri) {
        return map.get(uri);
    }
}
