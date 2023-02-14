package view;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import webserver.HttpResponse;

import java.io.IOException;

public class HandlebarsViewRenderer implements ViewRenderer {
    private final String sourcePath;

    public HandlebarsViewRenderer(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    @Override
    public void render(HttpResponse httpResponse) throws IOException {
        render(httpResponse, null);
    }

    @Override
    public void render(HttpResponse httpResponse, Object context) throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        Template template = handlebars.compile(sourcePath);
        String page = template.apply(context);

        httpResponse.addHeader("Content-Type", "text/html;charset=utf-8");
        httpResponse.changeBody(page.getBytes());
    }
}
