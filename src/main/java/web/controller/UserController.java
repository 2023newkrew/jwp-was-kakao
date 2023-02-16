package web.controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import web.db.DataBase;
import web.exception.AuthErrorCode;
import web.exception.BusinessException;
import framework.annotation.MyRequestMapping;
import framework.controller.Controller;
import framework.request.HttpCookie;
import framework.request.Request;
import framework.response.ContentType;
import framework.response.Response;
import framework.utils.FileIoUtils;
import web.model.User;
import web.service.UserService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof BusinessException) {
                throw (BusinessException) e.getTargetException();
            }
            else {
                e.getTargetException().printStackTrace();
                throw new RuntimeException();
            }
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
    }

    @MyRequestMapping(uri = "/user/login.html")
    public Response handleUserLoginPage(Request request) throws IOException, URISyntaxException {
        if (userService.isUserLoggedIn(request.getCookie())) {
            return Response.found().location("/index.html").build();
        }
        byte[] body = FileIoUtils.loadFileFromClasspath("templates/user/login.html");
        return Response.ok().contentType(ContentType.HTML).body(body).build();
    }

    @MyRequestMapping(uri = "/user/profile.html")
    public Response handleUserProfile(Request request) throws IOException {
        Handlebars handlebars = getHandlebars(TEMPLATE_PREFIX, TEMPLATE_SUFFIX);
        User user = userService.getUserFromCookie(request.getCookie());

        Template template = handlebars.compile("user/profile");
        String profilePage = template.apply(user);
        return Response.ok().body(profilePage).build();
    }

    @MyRequestMapping(uri = "/user/list.html")
    public Response handleUserList(Request request) throws IOException {
        Handlebars handlebars = getHandlebars(TEMPLATE_PREFIX, TEMPLATE_SUFFIX);
        if (!userService.isUserLoggedIn(request.getCookie())) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED);
        }

        List<User> users = new ArrayList<>(DataBase.findAll());
        Template template = handlebars.compile("user/list");
        String profilePage = template.apply(Map.of("users", users));
        return Response.ok().body(profilePage).build();
    }

    private static Handlebars getHandlebars(String prefix, String suffix) {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix(prefix);
        loader.setSuffix(suffix);
        return new Handlebars(loader);
    }
}
