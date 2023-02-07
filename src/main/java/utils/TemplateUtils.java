package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import lombok.experimental.UtilityClass;
import model.TemplateLoadResult;
import model.dto.UserListViewDto;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static constant.DefaultConstant.DEFAULT_VIEW_FILE_TYPE;
import static constant.PathConstant.TEMPLATES;

@UtilityClass
public class TemplateUtils {
    private final int INITIAL_INDEX = 1;
    public TemplateLoadResult handleUserListTemplate() {
        try {
            Handlebars handlebars = getTemplateHandlerBars();
            Template template = handlebars.compile("user/list");
            List<UserListViewDto> userListViewDtos = mappingAllUserToUserViewDto(new AtomicInteger());

            return TemplateLoadResult.from(
                    template.apply(mappingAllUserToUserViewDto(new AtomicInteger(INITIAL_INDEX)))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Handlebars getTemplateHandlerBars() {
        return new Handlebars(getTemplateLoader());
    }

    private TemplateLoader getTemplateLoader() {
        TemplateLoader templateLoader = new ClassPathTemplateLoader();
        templateLoader.setPrefix(TEMPLATES.substring(1));
        templateLoader.setSuffix(DEFAULT_VIEW_FILE_TYPE);

        return templateLoader;
    }

    private List<UserListViewDto> mappingAllUserToUserViewDto(AtomicInteger index) {
        return DataBase.findAll().stream()
                .map(user -> new UserListViewDto(
                        (long) index.getAndIncrement(),
                        user.getUserId(),
                        user.getName(),
                        user.getEmail()))
                .collect(Collectors.toList());
    }
}
