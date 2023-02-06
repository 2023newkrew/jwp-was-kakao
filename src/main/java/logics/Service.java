package logics;

import db.DataBase;
import model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * contains business logic, which is similar to Service component in Spring Framework.
 */
public class Service {
    // Spring Framework 상으로는 아래 메소드 중 DataBase에 저장하는 역할을 Repository class로 분리하는 것이 맞을 수 있다만,
    //
    public void updateUser(String body){
        Map<String, String> bodyMap = new HashMap<>();
        for (String splitted : body.split("&")) {
            bodyMap.put(splitted.split("=")[0], splitted.split("=")[1]);
        }
        User user = new User(bodyMap.get("userId"), bodyMap.get("password"),
                bodyMap.get("name"), bodyMap.get("email"));
        DataBase.addUser(user);
    }
}
