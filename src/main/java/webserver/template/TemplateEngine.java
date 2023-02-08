package webserver.template;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.springframework.http.HttpStatus;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class TemplateEngine {

    private static final Handlebars handlebars;

    static {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        handlebars = new Handlebars(loader);
        handlebars.registerHelper("inc1", (context, options) -> String.valueOf(1 + (Integer)context));
    }

    private TemplateEngine(){};

    public static HttpResponse getTemplateResponse(String templateName, Object context) {
        try {
            Template template = handlebars.compile(templateName);
            String body = template.apply(context);
            return HttpResponse
                    .status(HttpStatus.OK)
                    .body(body);
        } catch (IOException e) {
            return HttpResponse
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error handling template " + templateName);
        }
    }
}
