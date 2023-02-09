package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.IOException;

public class RenderUtils {
    private static final String DEFAULT_PREFIX = "/templates";
    private static final String DEFAULT_SUFFIX = ".html";

    public static String renderData(String path, Object data) throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix(DEFAULT_PREFIX);
        loader.setSuffix(DEFAULT_SUFFIX);
        Handlebars handlebars = new Handlebars(loader);
        handlebars.registerHelper("inc", (value, options) ->
                (Integer) value + 1
        );
        Template template = handlebars.compile(path);
        return template.apply(data);
    }
}
