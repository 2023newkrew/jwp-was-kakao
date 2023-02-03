package controller;

import model.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.MyHeaders;
import utils.MyParams;
import utils.UserFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static db.DataBase.addUser;
import static utils.ResponseBodies.responseBody;
import static utils.ResponseHeaders.response200Header;
import static utils.ResponseHeaders.response302Header;

public class UserController  implements MyController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Override
    public boolean canHandle(MyHeaders headers, MyParams params, Extension extension) {
        String path = headers.get("path");
        return path.startsWith("/user");
    }

    @Override
    public void handle(MyHeaders headers, MyParams params, Extension extension, DataOutputStream dataOutputStream) {
        String path = headers.get("path");

        if(path.equals("/user/form.html") && headers.get("method").equals("POST")){
            createUser(path, params, dataOutputStream);
        }

        if(path.equals("/user/form.html") && headers.get("method").equals("GET")){
            form(headers, dataOutputStream);
        }
    }

    private void createUser(String path, MyParams params, DataOutputStream dataOutputStream){
        // Memory DB에 유저 데이터 저장
        addUser(UserFactory.createUser(params));
        response302Header(dataOutputStream, "/index.html");
    }

    private void form(MyHeaders headers, DataOutputStream dataOutputStream){
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("templates" + headers.get("path"));
            response200Header(dataOutputStream, headers.get("contentType"), body.length);
            responseBody(dataOutputStream, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
