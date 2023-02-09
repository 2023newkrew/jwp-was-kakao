package controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import service.UserService;
import was.annotation.*;
import was.domain.response.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class UserController {

    @Mapping(method = RequestMethod.GET, path = "/user/form.html")
    public static Optional<Response> form() {
        return Response.htmlFromFile("./templates/user/form.html");
    }

    @RequestBody
    @Mapping(method = RequestMethod.POST, path="/user/create")
    public static Optional<Response> createUser(String body){
        UserService.createUser(body);
        return Response.redirection("/index.html");
    }

    @Mapping(method = RequestMethod.GET, path = "/user/login.html")
    public static Optional<Response> login() {
        return Response.htmlFromFile("./templates/user/login.html");
    }

    @RequestBody
    @Mapping(method = RequestMethod.POST, path = "/user/login")
    public static Optional<Response> submit(String body) {
        UUID uuid = UserService.login(body).orElse(null);
        if (uuid == null) {
            return Response.redirection("/user/login_failed.html");
        }
        return Response.redirection("/index.html", "JSESSIONID=" + uuid);
    }

    @RequestHeader
    @Mapping(method = RequestMethod.GET, path = "/user/list")
    public static Optional<Response> list(Map<String, String> headers) throws IOException {
        String body = UserService.list(headers).orElse(null);
        if (body == null) {
            return Response.redirection("/index.html");
        }
        return Response.html(body);
    }
}
