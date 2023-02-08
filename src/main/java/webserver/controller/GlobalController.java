package webserver.controller;

import lombok.RequiredArgsConstructor;
import model.LoginRequest;
import model.User;
import model.UserRequest;
import utils.DynamicTemplateLoader;
import utils.FileIoUtils;
import webserver.FileType;
import webserver.request.Method;
import webserver.request.Request;
import webserver.response.Response;
import webserver.service.SessionService;
import webserver.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
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
    public Response loginPost(Request request) {
        Optional<User> user = userService.login(LoginRequest.of(request.getRequestBodyAsQueryString()));
        if (user.isPresent()) {
            String cookie = sessionService.register(user.get());

            Response response = Response.found(new byte[0], FileType.HTML, "/index.html");
            response.setCookie(cookie, "/");
            return response;
        }
        return Response.found(new byte[0], FileType.HTML, "/user/login_failed.html");
    }

    @CustomRequestMapping(method = Method.GET, path = "/user/login")
    public Response loginGet(Request request) throws IOException, URISyntaxException {
        if (request.getCookie().isPresent() && sessionService.find(request.getCookie().get()).isPresent()) {
            return Response.found(new byte[0], FileType.HTML, "/index.html");
        }
        return Response.ok(FileIoUtils.loadFileFromClasspath("./templates/user/login.html"), FileType.HTML);
    }

    @CustomRequestMapping(method = Method.GET, path = "/user/list")
    public Response list(Request request) throws IOException {
        Optional<String> cookie = request.getCookie();
        if (cookie.isEmpty() || sessionService.find(cookie.get()).isEmpty()) {
            return Response.found(new byte[0], FileType.HTML, "/user/login.html");
        }

        Collection<User> users = userService.findAll();
        String htmlPage = DynamicTemplateLoader.loadHtml(users, "user/list");
        return Response.ok(htmlPage.getBytes(), FileType.HTML);
    }
}
