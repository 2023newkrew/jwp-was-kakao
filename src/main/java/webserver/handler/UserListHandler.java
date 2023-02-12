package webserver.handler;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import model.User;
import service.UserService;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class UserListHandler extends AbstractHandler {

    @Override
    protected void doGet(final HttpRequest request, final HttpResponse response) {
        if (Objects.isNull(request.getCookie("logined"))) {
            response.setStatus(HttpStatus.FOUND);
            response.setHeader("Location", "/index.html");
            return;
        }
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        Template template;
        try {
            template = handlebars.compile("user/list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<User> users = UserService.showUserList();
        try {
            String content = template.apply(users);
            response.setBody(content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.setStatus(HttpStatus.OK);
    }

    @Override
    protected void doPost(final HttpRequest request, final HttpResponse response) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
