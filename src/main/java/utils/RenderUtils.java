package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.IOException;

public class RenderUtils {
    private static final String DEFAULT_PREFIX = "/templates";
    private static final String DEFAULT_SUFFIX = ".html";

    private static Handlebars createHandlebars() {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix(DEFAULT_PREFIX);
        loader.setSuffix(DEFAULT_SUFFIX);
        return new Handlebars(loader);
    }

    public static String renderListData(String path, Object data) throws IOException {
        Handlebars handlebars = createHandlebars();
        handlebars.registerHelper("inc", (value, options) ->
                (Integer) value + 1
        );
        Template template = handlebars.compile(path);
        return template.apply(data);
    }

    public static String renderErrorPageData(String path, boolean existError) throws IOException {
        Handlebars handlebars = createHandlebars();
        handlebars.registerHelper("existError", (value, options) ->
                existError
        );
        Template template = handlebars.compile(path);
        return template.apply(existError);
    }
}
