package webserver.handler;

import webserver.request.Method;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Arrays;

import static webserver.request.Method.GET;
import static webserver.request.Method.POST;

public enum HandlerMapping {

    BASE_URL(GET, "/", new BaseHandler()),
    CREATE_USER(POST, "/user/create", new CreateUserHandler()),
    LOGIN_USER(POST, "/user/login", new LoginUserHandler());

    private final Method method;
    private final String path;
    private final Handler handler;

    HandlerMapping(Method method, String path, Handler handler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    private static HandlerMapping findHandler(Request request) {
        Method method = request.getMethod();
        String path = request.getPath();
        return Arrays.stream(values())
                .filter(handlerMapping -> handlerMapping.method == method && handlerMapping.path.equals(path))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Response handle(Request request) {
        HandlerMapping handlerMapping = findHandler(request);
        return handlerMapping.handler.apply(request);
    }
}
