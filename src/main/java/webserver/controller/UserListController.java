package webserver.controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import java.io.IOException;
import java.util.Map;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.Session;

public class UserListController implements Controller {
    @Override
    public boolean isHandleable(HttpRequest request) {
        return request.getPath().equals("/user/list");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        Session session = request.getSession();

        if (session == null) {
            return HttpResponse.redirect("/user/login.html");
        }

        return createUserListPageResponse();
    }

    private static HttpResponse createUserListPageResponse() {
        try {
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/templates");
            loader.setSuffix(".html");
            Handlebars handlebars = new Handlebars(loader);
            Template template = handlebars.compile("user/list");
            String listPage = template.apply(Map.of("users", DataBase.findAll()));
            return HttpResponse.ok(listPage.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
