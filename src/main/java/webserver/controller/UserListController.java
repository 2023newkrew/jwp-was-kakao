package webserver.controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import model.User;
import utils.FileIoUtils;
import webserver.HttpCookie;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.service.UserService;

public class UserListController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        HttpCookie httpCookie = new HttpCookie(httpRequest.getHeader("Cookie"));
        boolean logined = Optional
                .ofNullable(httpCookie.getCookie("logined"))
                .orElse("false").equals("true");
        if (logined) {
            try {
                TemplateLoader loader = new ClassPathTemplateLoader();
                loader.setPrefix("/templates");
                loader.setSuffix(".html");
                Handlebars handlebars = new Handlebars(loader);
                Template template = handlebars.compile("user/list");
                List<User> userList = UserService.getUserList();
                String userListPage = template.apply(userList);
                return HttpResponse.ok(httpRequest, userListPage.getBytes(), "text/html;charset=utf-8");
            } catch (IOException e) {
                return HttpResponse.pageNotFound();
            }
        }
        return HttpResponse.redirect(httpRequest, "http://localhost:8080/user/login.html");
    }
}
