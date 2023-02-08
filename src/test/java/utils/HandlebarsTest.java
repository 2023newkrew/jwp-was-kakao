package utils;

import app.user.User;
import app.user.dto.UserResponseDto;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlebarsTest {
    private static final Logger log = LoggerFactory.getLogger(HandlebarsTest.class);
    public static final Helper<Object> addOne = new Helper<>() {
        @Override
        public Object apply(Object context, Options options) throws IOException {
            Integer idx = (Integer) context;
            return idx + 1;
        }
    };

    @Test
    void name() throws Exception {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        Template template = handlebars.compile("/user/profile");

        User user = User.builder()
                .userId("javajigi")
                .password("password")
                .name("자바지기")
                .email("javajigi@gmail.com")
                .build();
        String profilePage = template.apply(user);
        log.info("ProfilePage : {}", profilePage);
    }


    @Test
    void list() throws Exception {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);
        handlebars.registerHelper("addOne", addOne);
        Template template = handlebars.compile("/user/list");
        User user = User.builder()
                .userId("javajigi")
                .password("password")
                .name("자바지기")
                .email("javajigi@gmail.com")
                .build();
        List<UserResponseDto> users = List.of(UserResponseDto.of(user));
        String profilePage = template.apply(users);
        log.info("ProfilePage : {}", profilePage);
    }
}
