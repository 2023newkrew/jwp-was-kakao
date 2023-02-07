package webserver.controller;

import lombok.RequiredArgsConstructor;
import model.LoginRequest;
import model.User;
import model.UserRequest;
import webserver.FileType;
import webserver.request.Method;
import webserver.request.Request;
import webserver.response.Response;
import webserver.service.SessionService;
import webserver.service.UserService;

import java.util.Optional;

@RequiredArgsConstructor
public class GlobalController {
    private static final String BASE_STRING = "Hello world";
    private final UserService userService;
    private final SessionService sessionService;

    @CustomRequestMapping(method = Method.GET)
    public Response base(Request request) {
        return Response.ok(BASE_STRING.getBytes(), FileType.HTML);
    }

    @CustomRequestMapping(method = Method.POST, path = "/user/create")
    public Response createByPost(Request request) {
        userService.create(UserRequest.of(request.getRequestBodyAsQueryString()));
        return Response.found(new byte[0], request.getRequestFileType(), "/index.html");
    }

    @CustomRequestMapping(method = Method.GET, path = "/user/create")
    public Response createByGet(Request request) {
        userService.create(UserRequest.of(request.getQueryString()));
        return Response.found(new byte[0], request.getRequestFileType(), "/index.html");
    }

    @CustomRequestMapping(method = Method.POST, path = "/user/login")
    public Response login(Request request) {
        Optional<User> user = userService.login(LoginRequest.of(request.getRequestBodyAsQueryString()));
        if (user.isPresent()) {
            String cookie = sessionService.register(user.get().getUserId());

            Response response = Response.found(new byte[0], FileType.HTML, "/index.html");
            response.setCookie(cookie, "/");
            return response;
        }
        return Response.found(new byte[0], FileType.HTML, "/user/login_failed.html");
    }


}
