package controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.User;
import service.UserService;
import was.annotation.Controller;
import was.annotation.Mapping;
import was.annotation.RequestBody;
import was.annotation.RequestMethod;
import was.domain.Cookie;
import was.domain.Cookies;
import was.domain.request.Request;
import was.domain.response.Response;
import was.domain.response.ResponseHeader;
import was.domain.response.StatusCode;
import was.domain.response.Version;

import java.util.Optional;

@Controller
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserController {
    @RequestBody
    @Mapping(method = RequestMethod.POST, path = "/user/create")
    public static Optional<Response> createUser(Request request) {
        UserService.createUser(request.getBody());
        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.FOUND)
                .responseHeader(ResponseHeader.builder()
                        .location("/index.html")
                        .build())
                .build());
    }

    @RequestBody
    @Mapping(method = RequestMethod.POST, path = "/user/login")
    public static Optional<Response> login(Request request) {
        User user = UserService.getMatchedUser(request.getBody()).orElse(null);
        Cookie cookie = user == null ? null : Cookies.createCookie(user);
        String location = user == null ? "/user/login_failed.html" : "/index.html";

        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.FOUND)
                .responseHeader(ResponseHeader.builder()
                        .location(location)
                        .cookie(cookie)
                        .build())
                .build());
    }
}
