package webserver;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import controller.UserController;
import model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HandlebarsTest;

public class TemplateTest {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Test
    void name() throws Exception {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        Template template = handlebars.compile("user/profile");

        User user = new User("cu", "password", "이동규", "brainbackdoor@gmail.com");
        String profilePage = template.apply(user);
        logger.debug("ProfilePage : {}", profilePage);
    }
}
