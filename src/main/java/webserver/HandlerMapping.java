package webserver;

import webserver.handler.Handler;
import webserver.handler.UserHandler;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private final Map<String, Handler> map = new HashMap<>() {{
        put("/user/create", new UserHandler());
    }};

    public Handler getHandler(String uri) {
        return map.get(uri);
    }
}
