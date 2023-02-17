package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.IOException;
import webserver.FilenameExtension;
import webserver.response.HttpResponse;

public class TemplateEngine {

    private static final Handlebars handlebars;

    static {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("./template");
        loader.setSuffix(".html");
        handlebars = new Handlebars(loader);
    }

    public static HttpResponse getTemplateResponse(String templateName, Object context) {
        try {
            Template template = handlebars.compile(templateName);
            String body = template.apply(context);
            return HttpResponse.ok(body.getBytes(), FilenameExtension.HTML);
        } catch (IOException e) {
            return HttpResponse.internalServerError();
        }
    }

}
