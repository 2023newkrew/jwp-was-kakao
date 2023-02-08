package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import model.User;
import model.dto.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import model.dto.MyHeaders;
import model.dto.MyParams;
import utils.UserFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static db.DataBase.addUser;
import static db.DataBase.findAll;
import static model.dto.ResponseBodies.responseBody;
import static model.dto.ResponseHeaders.response200Header;
import static model.dto.ResponseHeaders.response302Header;

public class UserController  implements MyController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Override
    public boolean canHandle(MyHeaders headers, MyParams params) {
        String path = headers.get("path");
        return path.startsWith("/user");
    }

    @Override
    public void handle(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream) {
        String path = headers.get("path");
        String cookie = headers.get("cookie");
        String contentType = headers.get("contentType");

        if(path.equals("/user/create") && headers.get("method").equals("POST")){
            createUser(params, cookie, dataOutputStream);
        }

        if(path.equals("/user/form.html") && headers.get("method").equals("GET")){
            form(path, contentType, cookie, dataOutputStream);
        }

        if(path.equals("/user/list") && headers.get("method").equals("GET")){
            getUserList(dataOutputStream, contentType, cookie);
        }
    }

    private void getUserList(DataOutputStream dataOutputStream, String contentType, String cookie) {
        try{
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/templates");
            loader.setSuffix(".hbs");
            Handlebars handlebars = new Handlebars(loader);

            Template template = handlebars.compile("user/list");

            // handlebars 모델 전송용 dto
            Users users = new Users(findAll().stream().collect(Collectors.toList()));

            String userList = template.apply(users);
            byte[] body = userList.getBytes();

            response200Header(dataOutputStream, contentType, cookie, body.length);
            responseBody(dataOutputStream, userList.getBytes());
        } catch(IOException e){
            logger.error(e.getMessage());
        }
    }

    private void createUser(MyParams params, String cookie, DataOutputStream dataOutputStream){
        // Memory DB에 유저 데이터 저장
        addUser(UserFactory.createUser(params));
        response302Header(dataOutputStream, cookie, "/index.html");
    }

    private void form(String path, String contentType, String cookie, DataOutputStream dataOutputStream){
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("templates" + path);
            response200Header(dataOutputStream, contentType, cookie, body.length);
            responseBody(dataOutputStream, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
