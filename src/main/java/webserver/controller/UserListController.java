package webserver.controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import model.User;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseContentType;
import webserver.response.HttpResponseStatus;
import webserver.utils.ResponseUtil;

import java.io.IOException;
import java.util.Collection;

import static webserver.request.HttpRequestMethod.GET;
import static webserver.response.HttpResponseStatus.OK;

public class UserListController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        TemplateLoader loader = new ClassPathTemplateLoader("/templates", ".html");
        Handlebars handlebars = new Handlebars(loader);
        try {
            Template template = handlebars.compile("user/list");
            Collection<User> users = DataBase.findAll();
            String userListPage = template.apply(users);
            ResponseUtil.response200(response, userListPage.getBytes(), HttpResponseContentType.HTML);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isMatch(HttpRequest request) {
        return request.getMethod() == GET
                && request.getUri().getPath().equals("/user/list");
    }

    @Override
    public HttpResponseStatus getSuccessCode() {
        return OK;
    }
}
