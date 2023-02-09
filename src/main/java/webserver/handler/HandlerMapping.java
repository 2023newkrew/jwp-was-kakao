package webserver.handler;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.util.Arrays;
import org.springframework.http.HttpMethod;
import webserver.response.HttpResponse;
import webserver.request.HttpRequest;

public enum HandlerMapping {

    BASE(GET, "/", new BaseHandler()),
    CREATE_USER_FROM_GET(GET, "/user/create", new CreateUserHandler()),
    CREATE_USER_FROM_POST(POST, "/user/create", new CreateUserHandler()),
    LOGIN(POST, "/user/login", new LoginHandler());

    private final HttpMethod method;
    private final String path;
    private final Handler handler;

    HandlerMapping(HttpMethod method, String path, Handler handler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    private static Handler getHandler(HttpRequest request) {
        HttpMethod method = request.getHttpMethod();
        String path = request.getPath();
        return Arrays.stream(values())
                .filter(handlerMapping -> handlerMapping.method.equals(method) &&
                                          handlerMapping.path.equals(path))
                .map(handlerMapping -> handlerMapping.handler)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static HttpResponse handle(HttpRequest request) {
        Handler mappedHandler = getHandler(request);
        return mappedHandler.applyRequest(request);
    }
}
