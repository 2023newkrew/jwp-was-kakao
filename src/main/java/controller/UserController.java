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
import model.User;
import service.UserService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UserController implements Controller {

    public static final String TEMPLATE_PREFIX = "/templates";
    public static final String TEMPLATE_SUFFIX = ".html";
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
            e.printStackTrace();
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
        HttpCookie cookie = userService.loginUser(request.getRequestParams());
        return Response.found().location("/index.html").setCookie(cookie).build();
//        String body = "";
//        try {
//            body = new String(Objects.requireNonNull(FileIoUtils.loadFileFromClasspath("templates/user/login_failed.html")), StandardCharsets.UTF_8);
//        } catch (IOException | NullPointerException | URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return Response.unauthorized().body(body).build();
    }

    @MyRequestMapping(uri = "/user/profile.html")
    public Response handleUserProfile(Request request) {
        Handlebars handlebars = getHandlebars(TEMPLATE_PREFIX, TEMPLATE_SUFFIX);
        User user = userService.getUserFromCookie(request.getCookie());

        try {
            Template template = handlebars.compile("user/profile");
            String profilePage = template.apply(user);
            return Response.ok().body(profilePage).build();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @MyRequestMapping(uri = "/user/list.html")
    public Response handleUserList(Request request) {
        Handlebars handlebars = getHandlebars(TEMPLATE_PREFIX, TEMPLATE_SUFFIX);
        if (!userService.isUserLoggedIn(request.getCookie())) {
            throw new RuntimeException();
        }

        try {
            List<User> users = new ArrayList<>(DataBase.findAll());
            Template template = handlebars.compile("user/list");
            String profilePage = template.apply(Map.of("users", users));
            return Response.ok().body(profilePage).build();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private static Handlebars getHandlebars(String prefix, String suffix) {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix(prefix);
        loader.setSuffix(suffix);
        return new Handlebars(loader);
    }
}
