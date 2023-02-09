package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

import java.io.IOException;

public class LoginPageController implements Controller {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException {
        HttpSession session = httpRequest.getSession();
        if (session != null && session.getAttribute("user") != null) {
            String location = "/index.html";
            httpResponse.sendRedirect(location);
            throw new RedirectException(location);
        }

        String sourcePath = httpRequest.getUrl();

        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        try {
            Template template = handlebars.compile(sourcePath);
            String page = template.apply(null);
            httpResponse.changeBody(page.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
