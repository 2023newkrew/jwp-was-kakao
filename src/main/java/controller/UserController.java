package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import framework.annotation.MyRequestMapping;
import framework.controller.Controller;
import framework.request.HttpCookie;
import framework.request.Request;
import framework.response.ContentType;
import framework.response.Response;
import framework.utils.FileIoUtils;
import model.User;
import service.Session;
import service.SessionHandler;
import service.UserService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class UserController implements Controller {

    private final Map<String, Method> map = new HashMap<>();

    private final UserService userService = UserService.getInstance();

    private static class LazyHolder {
        public static final UserController INSTANCE = new UserController();
    }
    private UserController() {
        List<Method> methodList = List.of(this.getClass().getMethods());
        for (Method method: methodList) {
            MyRequestMapping myRequestMapping = method.getAnnotation(MyRequestMapping.class);
            if (myRequestMapping != null) {
                map.put(myRequestMapping.uri(), method);
            }
        }
    }

    public static UserController getInstance() {
        return LazyHolder.INSTANCE;
    }

    public boolean canHandle(String uri) {
        return map.containsKey(uri);
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            return (Response) map.get(request.getUri()).invoke(UserController.getInstance(), request);
        } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @MyRequestMapping(uri = "/")
    public Response handleRootPage(Request request) {
        return Response.ok().contentType(ContentType.HTML).body("Hello world").build();
    }

    @MyRequestMapping(uri = "/user/create")
    public Response handleUserCreate(Request request) {
        userService.createUser(request.getRequestParams());
        return Response.found().location("/index.html").build();
    }

    @MyRequestMapping(uri = "/user/login")
    public Response handleUserLogin(Request request) {
        Map<String, String> params = request.getRequestParams();
        User user = DataBase.findUserById(params.get("userId"));
        String body = "";
        try {
            body = new String(Objects.requireNonNull(FileIoUtils.loadFileFromClasspath("templates/user/login_failed.html")), StandardCharsets.UTF_8);
        } catch (IOException | NullPointerException | URISyntaxException e) {
            e.printStackTrace();
        }
        if (user == null) {
            return Response.unauthorized().body(body).build();
        }
        if (user.checkPassword(params.get("password"))) {
            UUID uuid = UUID.randomUUID();
            Session session = new Session(Map.of("user", user));
            SessionHandler.getInstance().saveSession(uuid, session);
            HttpCookie httpCookie = HttpCookie.from(Map.of("JSESSIONID", uuid.toString()));
            return Response.found().location("/index.html").setCookie(httpCookie).build();
        }
        return Response.unauthorized().body(body).build();
    }

    @MyRequestMapping(uri = "/user/profile.html")
    public Response handleUserProfile(Request request) {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        try {
            UUID uuid = UUID.fromString(request.getCookie().get("JSESSIONID"));
            Session session = SessionHandler.getInstance().getSession(uuid);
            User user = (User) session.get("user");

            Template template = handlebars.compile("user/profile");
            String profilePage = template.apply(user);
            return Response.ok().body(profilePage).build();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return Response.unauthorized().body("Unauthorized").build();
    }

    @MyRequestMapping(uri = "/user/list.html")
    public Response handleUserList(Request request) {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        try {
            UUID uuid = UUID.fromString(request.getCookie().get("JSESSIONID"));
            Session session = SessionHandler.getInstance().getSession(uuid);
            User user = (User) session.get("user");

            List<User> users = new ArrayList<>(DataBase.findAll());

            Template template = handlebars.compile("user/list");
            String profilePage = template.apply(Map.of("users", users));
            return Response.ok().body(profilePage).build();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return Response.unauthorized().body("Unauthorized").build();
    }
}
