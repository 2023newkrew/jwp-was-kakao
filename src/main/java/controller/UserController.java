package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.User;
import service.UserService;
import was.annotation.Controller;
import was.annotation.Mapping;
import was.annotation.RequestBody;
import was.annotation.RequestMethod;
import was.domain.Cookie;
import was.domain.request.Request;
import was.domain.response.Response;
import was.domain.response.ResponseHeader;
import was.domain.response.StatusCode;
import was.domain.response.Version;
import was.domain.session.Session;
import was.domain.session.SessionManager;

import java.io.IOException;
import java.util.Optional;

@Controller
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserController {
    @RequestBody
    @Mapping(method = RequestMethod.POST, path = "/user/create")
    public static Optional<Response> createUser(Request request) {
        UserService.createUser(request.getBody());
        return redirectToIndexResponse();
    }

    @RequestBody
    @Mapping(method = RequestMethod.POST, path = "/user/login")
    public static Optional<Response> login(Request request) {
        if (request.getCookie() != null && UserService.isValidUser(getUserFormRequest(request))) {
            return redirectToIndexResponse();
        }

        User user = UserService.getMatchedUser(request.getBody()).orElse(null);

        if (user == null) {
            return redirectToLoginFailedResponse();
        }

        Session session = SessionManager.createSession();
        session.setAttribute("user", user);
        Cookie cookie = new Cookie(session.getId(), "/");

        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.FOUND)
                .responseHeader(ResponseHeader.builder()
                        .location("/index.html")
                        .cookie(cookie)
                        .build())
                .build());
    }

    @Mapping(method = RequestMethod.GET, path = "/user/list")
    public static Optional<Response> list(Request request) {
        if (request.getCookie() == null || !UserService.isValidUser(getUserFormRequest(request))) {
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.FOUND)
                    .responseHeader(ResponseHeader.builder()
                            .location("/user/login.html")
                            .build())
                    .build());
        }

        String list = getUserListFile();
        if (list.equals(""))
            return Optional.ofNullable(null);

        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.OK)
                .responseHeader(ResponseHeader.builder()
                        .contentLength(list.length())
                        .contentType("text/html")
                        .build())
                .body(list.getBytes())
                .build());
    }

    private static String getUserListFile() {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        try {
            Template template = handlebars.compile("user/list");
            return template.apply(UserService.findAllUser());
        } catch (IOException e) {
            return "";
        }
    }

    private static Optional<Response> redirectToIndexResponse() {
        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.FOUND)
                .responseHeader(ResponseHeader.builder()
                        .location("/index.html")
                        .build())
                .build());
    }

    private static Optional<Response> redirectToLoginFailedResponse() {
        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.FOUND)
                .responseHeader(ResponseHeader.builder()
                        .location("/user/login_failed.html")
                        .build())
                .build());
    }

    private static User getUserFormRequest(Request request) {
        return (User) SessionManager.getSession(request.getCookie().getUuid()).getAttribute("user");
    }
}
