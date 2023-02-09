package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import java.io.IOException;

public class HandlebarsUtils {

    private static final Handlebars H;

    private static final String PREFIX = "/templates";

    private static final String SUFFIX = ".html";

    static {
        TemplateLoader loader = new ClassPathTemplateLoader(PREFIX, SUFFIX);
        H = new Handlebars(loader);
    }
    private HandlebarsUtils() {}

    public static String getView(String path, Object model) {
        try {
            Template template = H.compile(path);
            return template.apply(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
