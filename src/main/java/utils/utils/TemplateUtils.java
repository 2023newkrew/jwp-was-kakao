package utils.utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import exception.TemplateCannotLoadedException;
import lombok.experimental.UtilityClass;
import model.dto.user.UserListViewDto;
import model.dto.view.TemplateLoadResult;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static constant.DefaultConstant.*;
import static constant.PathConstant.*;

@UtilityClass
public class TemplateUtils {
    private final String TEMPLATE_PREFIX = TEMPLATES.substring(1);

    public TemplateLoadResult createTemplate(String path, Object context) {
        try {
            Handlebars handlebars = getTemplateHandlerBars();
            Template template = handlebars.compile(path);

            return TemplateLoadResult.from(
                    template.apply(context)
            );
        } catch (IOException e) {
            throw new TemplateCannotLoadedException(e);
        }
    }

    private Handlebars getTemplateHandlerBars() {
        return new Handlebars(getTemplateLoader());
    }

    private TemplateLoader getTemplateLoader() {
        TemplateLoader templateLoader = new ClassPathTemplateLoader();
        templateLoader.setPrefix(TEMPLATE_PREFIX);
        templateLoader.setSuffix(DEFAULT_VIEW_FILE_TYPE);

        return templateLoader;
    }
}
