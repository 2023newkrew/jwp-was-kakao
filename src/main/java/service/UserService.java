package service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import exception.UserNotFoundException;
import model.User;

import java.io.IOException;

public class UserService {

    public void join(User user) {
        DataBase.addUser(user);
    }

    public User findUserById(User user) {
        return DataBase.findUserById(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("아이디와 비밀번호가 일치하지 않습니다."));
    }

    public String getUserListHtml() throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        Template template = handlebars.compile("user/list");

        return template.apply(DataBase.findAll());
    }

}
