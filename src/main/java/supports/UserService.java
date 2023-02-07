package supports;

import db.DataBase;
import model.User;
import utils.IoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class UserService {

    private static final String AND = "&";
    private static final String EQUAL = "=";

    public UserService() {
    }

    public void saveUser(BufferedReader br, HttpParser httpParser) throws IOException {
        Integer contentLength = httpParser.getContentLength();
        String userBody = IoUtils.readData(br, contentLength);
        HashMap<String, String> queryParam = parseQueryParameter(userBody);

        User user = new User(
                queryParam.get("userId"),
                queryParam.get("password"),
                queryParam.get("name"),
                queryParam.get("email")
        );
        DataBase.addUser(user);
        System.out.println(DataBase.findAll());
    }

    private HashMap<String, String> parseQueryParameter(String userBody) {
        HashMap<String, String> result = new HashMap<>();

        for (String info : userBody.split(AND)) {
            String key = info.split(EQUAL)[0];
            String value = info.split(EQUAL)[1];
            result.put(key, value);
        }

        return result;
    }
}
