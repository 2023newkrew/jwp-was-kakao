package webserver.handler;

import webserver.exception.InternalServerErrorException;
import webserver.exception.NotFoundException;
import webserver.request.Method;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Arrays;

import static webserver.request.Method.GET;
import static webserver.request.Method.POST;

public enum HandlerMapping {

    BASE_URL(GET, "/", new BaseHandler()),
    CREATE_USER(POST, "/user/create", new CreateUserHandler()),
    USER_LIST(GET, "/user/list", new UserListHandler()),
    LOGIN_USER(POST, "/user/login", new LoginUserHandler()),
    LOGIN_CHECK(GET, "/user/login", new LoginCheckHandler());

    private final Method method;
    private final String path;
    private final Handler handler;

    HandlerMapping(Method method, String path, Handler handler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    public static Response handle(Request request) {
        try {
            HandlerMapping handlerMapping = findHandler(request);
            return handlerMapping.handler.apply(request);
        } catch (NotFoundException e) {
            return Response.notFound();
        } catch (InternalServerErrorException e) {
            return Response.internalServerError();
        }
    }

    private static HandlerMapping findHandler(Request request) {
        Method method = request.getMethod();
        String path = request.getPath();
        return Arrays.stream(values())
            .filter(handlerMapping -> handlerMapping.method == method && handlerMapping.path.equals(path))
            .findAny()
            .orElseThrow(NotFoundException::new);
    }
}
