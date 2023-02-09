package logics.Service;

import db.DataBase;
import model.User;
import model.UserDto;

import java.util.*;

/**
 * contains logic related to User.
 */
public class UserService {
    public static final UserService instance = new UserService();
    private UserService(){}
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


    public List<UserDto> getUserInformation(){
        List<UserDto> result = new ArrayList<>();
        int index = 1;
        for (User user : DataBase.findAll()){
            result.add(UserDto.of(user, index));
            index++;
        }
        return result;
    }

    public boolean verifyLogin(final Map<String, String> bodyMap){
        String userId = bodyMap.get("userId");
        String password = bodyMap.get("password");
        if (Objects.isNull(DataBase.findUserById(userId)) ||
                !DataBase.findUserById(userId).getPassword().equals(password)){
            return false;
        }
        return true;
    }
}
