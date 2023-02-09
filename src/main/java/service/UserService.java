package service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.User;
import model.UserRequest;
import was.utils.ParamsParser;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {
    public static Long createUser(String body) {
        return createUser(ParamsParser.from(body).getParams());
    }

    public static Long createUser(Map<String,String> params){
        UserRequest userRequest = UserRequest.builder()
                .userId(params.get("userId"))
                .password(params.get("password"))
                .name(params.get("name"))
                .email(params.get("email"))
                .build();
        return DataBase.addUser(userRequest).orElseThrow(RuntimeException::new);
    }

    public static Optional<UUID> login(String body) {
        return login(ParamsParser.from(body).getParams());
    }

    public static Optional<UUID> login(Map<String, String> params) {
        User user = DataBase.findUserByUserId(params.get("userId")).orElse(null);
        if (user != null && user.isPassword(params.get("password"))) {
            return Optional.of(UUID.randomUUID());
        }
        return Optional.empty();
    }

    public static Optional<String> list(Map<String, String> headers) {
        if (!headers.containsKey("Cookie")) {
            return Optional.empty();
        }

        try {
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/templates");
            loader.setSuffix(".html");
            Handlebars handlebars = new Handlebars(loader);
            Template template = handlebars.compile("user/profile");
            return Optional.of(template.apply(DataBase.findAll()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
