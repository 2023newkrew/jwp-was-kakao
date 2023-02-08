package logics;

import db.DataBase;
import model.User;
import utils.session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * contains business logic, which is similar to Service component in Spring Framework.
 */
public class Service {
    // Spring Framework 상으로는 아래 메소드 중 DataBase에 저장하는 역할을 Repository class로 분리하는 것이 맞을 수 있다만,
    // 이 코드에서는 DataBase가 Repository 역할을 한다고 생각하고 아래와 같이 코드를 작성하였다.

    /**
     * Update User into program's DB when given body containing information of user.
     * @param body should contain information of user with URL parameter sending method(param1=param1&param2=param2...).
     */
    public void updateUser(String body){
        try {
            User user = parseUser(body);
            DataBase.addUser(user);
        } catch(IndexOutOfBoundsException e){ // parseUser에서 split이 되지 않는 경우
            throw new IllegalArgumentException("body is not valid");
        }
    }

    private User parseUser(String body){
        Map<String, String> bodyMap = parseBody(body);
        return new User(bodyMap.get("userId"), bodyMap.get("password"),
                bodyMap.get("name"), bodyMap.get("email"));
    }

    private Map<String, String> parseBody(String body){
        Map<String, String> bodyMap = new HashMap<>();
        for (String splitted : body.split("&")) {
            bodyMap.put(splitted.split("=")[0], splitted.split("=")[1]);
        }
        return bodyMap;
    }

    public Session login(String body){
        try{
            Map<String, String> bodyMap = parseBody(body);
            return verifyLogin(bodyMap);
        } catch(IndexOutOfBoundsException e){
            throw new IllegalArgumentException("body is not valid");
        }
    }

    private Session verifyLogin(final Map<String, String> bodyMap){
        String userId = bodyMap.get("userId");
        String password = bodyMap.get("password");
        if (Objects.isNull(DataBase.findUserById(userId)) ||
                !DataBase.findUserById(userId).getPassword().equals(password)){
            throw new IllegalArgumentException("Invalid ID or Password");
        }
        return makeSession();
    }

    private Session makeSession(){
        return new Session.Builder().setRandomId().build();
    }
}
