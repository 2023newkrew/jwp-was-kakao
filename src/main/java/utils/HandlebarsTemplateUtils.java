package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import error.ApplicationException;

import java.io.IOException;

import static error.ErrorType.FILE_READ_FAILED;

public class HandlebarsTemplateUtils {

    private static final TemplateLoader templateLoader = new ClassPathTemplateLoader("/templates", ".html");

    public static String create(String resourcePath, Object context) {
        try {
            Handlebars handlebars = new Handlebars(templateLoader);
            Template template = handlebars.compile(resourcePath);

            return template.apply(new TemplateContext(context));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException(FILE_READ_FAILED, e.getMessage());
        }
    }

    private static class TemplateContext {
        private final Object object;

        public TemplateContext(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }
    }

}
