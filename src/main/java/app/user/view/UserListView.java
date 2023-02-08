package app.user.view;

import static infra.utils.response.ResponseUtils.make200Response;
import static infra.utils.response.ResponseUtilsConstants.HTML_EXTENSION;
import static infra.utils.response.ResponseUtilsConstants.TEMPLATES;
import static infra.utils.response.ResponseUtilsConstants.USER_LIST;

import app.user.dto.UserResponseDto;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;

public class UserListView {
    public static void make200ResponseUserListView(String contentType, DataOutputStream dataOutputStream,
                                                   List<UserResponseDto> users, Logger logger) {
        try {
            ClassPathTemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/" + TEMPLATES);
            loader.setSuffix(HTML_EXTENSION);
            Handlebars handlebars = new Handlebars(loader);
            handlebars.registerHelper("addOne", (context, options) -> (Integer) context + 1);
            Template template = handlebars.compile(USER_LIST);
            String apply = template.apply(users);
            make200Response(apply.getBytes(), contentType, dataOutputStream, logger);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
