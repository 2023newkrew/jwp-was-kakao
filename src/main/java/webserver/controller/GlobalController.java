package webserver.controller;

import lombok.RequiredArgsConstructor;
import model.UserRequest;
import webserver.FileType;
import webserver.request.Method;
import webserver.request.Request;
import webserver.response.Response;
import webserver.service.UserService;

@RequiredArgsConstructor
public class GlobalController {
    private static final String BASE_STRING = "Hello world";
    private final UserService userService;

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


}
