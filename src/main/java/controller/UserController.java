package controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import service.UserService;
import was.annotation.Controller;
import was.annotation.Mapping;
import was.annotation.RequestBody;
import was.annotation.RequestMethod;
import was.domain.response.Response;

import java.util.Optional;
import java.util.UUID;

@Controller
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserController {

    @Mapping(method = RequestMethod.GET, path = "/user/form.html")
    public static Optional<Response> form() {
        return Response.html("./templates/user/form.html");
    }

    @RequestBody
    @Mapping(method = RequestMethod.POST, path="/user/create")
    public static Optional<Response> createUser(String body){
        UserService.createUser(body);
        return Response.redirection("/index.html");
    }

    @Mapping(method = RequestMethod.GET, path = "/user/login.html")
    public static Optional<Response> login() {
        return Response.html("./templates/user/login.html");
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
}
