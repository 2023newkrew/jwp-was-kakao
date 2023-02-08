package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import java.io.IOException;

public class DynamicTemplateLoader {
    private static final String PREFIX = "/templates";
    private static final String SUFFIX = ".html";

    public static String loadHtml(Object data, String path) throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix(PREFIX);
        loader.setSuffix(SUFFIX);

        Handlebars handlebars = new Handlebars(loader);
        Template template = handlebars.compile(path);
        return template.apply(data);
    }
}
