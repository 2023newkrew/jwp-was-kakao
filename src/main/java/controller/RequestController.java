package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.Request;
import model.Response;
import model.User;
import model.Users;
import utils.FileIoUtils;

public class RequestController {
    private final TemplateLoader loader = new ClassPathTemplateLoader();

    public void createUser(Request request, Response response) throws URISyntaxException, IOException {
        User user = new User(
                request.getBodyValue("userId"),
                request.getBodyValue("password"),
                request.getBodyValue("name"),
                request.getBodyValue("email")
        );
        DataBase.addUser(user);
        response.found(new URI("/index.html"));
        response.send();
    }

    public void login(Request request, Response response) throws URISyntaxException, IOException {
        String userId = request.getBodyValue("userId");
        String password = request.getBodyValue("password");
        User user = DataBase.findUserById(userId);
        if (Objects.isNull(user) || !user.getPassword().equals(password)) {
            sendLoginFailPage(response);
            return;
        }
        request.getSession().setAttribute("user", user);
        response.found(new URI("/index.html"));
        response.send();
    }

    // Main

    public void sendMainPage(Request request, Response response) throws URISyntaxException, IOException {
        byte[] body = FileIoUtils.loadFileFromClasspath(request.getRoot() + "/index.html");
        sendPage(request, response, body);
    }

    // User

    public void sendLoginPage(Request request, Response response) throws URISyntaxException, IOException {
        byte[] body = FileIoUtils.loadFileFromClasspath(request.getRoot() + "/user/login.html");
        sendPage(request, response, body);
    }

    public void sendLoginFailedPage(Request request, Response response) throws URISyntaxException, IOException {
        byte[] body = FileIoUtils.loadFileFromClasspath(request.getRoot() + "/user/login_failed.html");
        sendPage(request, response, body);
    }

    public void sendSignupPage(Request request, Response response) throws URISyntaxException, IOException {
        byte[] body = FileIoUtils.loadFileFromClasspath(request.getRoot() + "/user/form.html");
        sendPage(request, response, body);
    }

    public void sendProfilePage(Request request, Response response) throws URISyntaxException, IOException {
        byte[] body = FileIoUtils.loadFileFromClasspath(request.getRoot() + "/user/profile.html");
        sendPage(request, response, body);
    }

    public void sendUserListPage(Request request, Response response) throws IOException {
        Template template = getTemplate(request, "/user/list");
        List<User> users = new ArrayList<>(DataBase.findAll());
//        User user = new User("cu", "password", "이도규", "park@park.com");
        byte[] body = template.apply(users).getBytes();
        sendPage(request, response, body);
    }

    // Qna

    public void sendQnaShowPage(Request request, Response response) throws URISyntaxException, IOException {
        byte[] body = FileIoUtils.loadFileFromClasspath(request.getRoot() + "/qna/show.html");
        sendPage(request, response, body);
    }

    public void sendQnaFormPage(Request request, Response response) throws URISyntaxException, IOException {
        byte[] body = FileIoUtils.loadFileFromClasspath(request.getRoot() + "/qna/form.html");
        sendPage(request, response, body);
    }

    // Static

    public void sendPage(Request request, Response response) throws IOException {
        byte[] body = request.getResponse();
        response.setBody(body, request.getAccept());
        response.send();
    }

    private void sendPage(Request request, Response response, byte[] body) throws IOException {
        response.setBody(body, request.getAccept());
        response.send();
    }

    // Private

    private void sendLoginFailPage(Response response) throws URISyntaxException, IOException {
        response.found(new URI("/user/login_failed.html"));
        response.send();
    }

    private Template getTemplate(Request request, String path) throws IOException {
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);
        return handlebars.compile(path);
    }
}
