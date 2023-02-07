package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import static constant.DefaultConstant.DEFAULT_VIEW_FILE_TYPE;
import static constant.PathConstant.TEMPLATES;

public class TemplateUtils {
    public void foo() {
        TemplateLoader templateLoader = new ClassPathTemplateLoader();
        templateLoader.setPrefix(TEMPLATES.substring(1));
        templateLoader.setSuffix(DEFAULT_VIEW_FILE_TYPE);

        Handlebars handlebars = new Handlebars(templateLoader);

    }
}
