package supports;

import db.DataBase;
import model.User;
import utils.IOUtils;
import utils.LogicValidatorUtils;
import webserver.RequestHandler;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private static final String AND = "&";
    private static final String EQUAL = "=";

    public UserService() {
    }

    public void saveUser(BufferedReader br, HttpParser httpParser) throws IOException {
        Integer contentLength = httpParser.getContentLength();
        String userBody = IOUtils.readData(br, contentLength);
        HashMap<String, String> queryParam = parseQueryParameter(userBody);

        User user = new User(
                queryParam.get("userId"),
                queryParam.get("password"),
                queryParam.get("name"),
                queryParam.get("email")
        );
        DataBase.addUser(user);
        RequestHandler.logger.info("User saved. : {}", DataBase.findUserById(queryParam.get("userId")));
    }

    private HashMap<String, String> parseQueryParameter(String userBody) {
        HashMap<String, String> result = new HashMap<>();

        LogicValidatorUtils.checkNull(userBody);
        for (String info : userBody.split(AND)) {
            LogicValidatorUtils.checkNull(info);
            String key = info.split(EQUAL)[0];
            String value = info.split(EQUAL)[1];
            result.put(key, value);
        }

        return result;
    }

    public Optional<UUID> loginUser(BufferedReader br, HttpParser httpParser) throws IOException {
        Integer contentLength = httpParser.getContentLength();
        String userBody = IOUtils.readData(br, contentLength);
        HashMap<String, String> queryParam = parseQueryParameter(userBody);

        User actualUser = DataBase.findUserById(queryParam.get("userId"));
        if (actualUser == null){
            RequestHandler.logger.error("아이디가 잘못되었습니다.");
            return Optional.empty();
        }

        if(!actualUser.matchPassword(queryParam.get("password"))){
            RequestHandler.logger.error("비밀번호가 잘못되었습니다.");
            return Optional.empty();
        }

        return Optional.of(UUID.randomUUID());
    }
}
