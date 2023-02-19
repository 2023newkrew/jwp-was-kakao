package logics.Service;

import db.DataBase;
import model.User;
import model.UserListDto;

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

    /**
     * Get user information list, excluding secret information such as password.
     * @return List of UserListDto
     */
    public List<UserListDto> getUserInformation(){
        List<UserListDto> result = new ArrayList<>();
        int index = 1;
        for (User user : DataBase.findAll()){
            result.add(UserListDto.of(user, index));
            index++;
        }
        return result;
    }

    /**
     * Verify whether login is valid or not.
     * @param bodyMap should contain value of key "userId" and "password".
     * @return true if given login information is valid, else return false.
     */
    public boolean verifyLogin(final Map<String, String> bodyMap){
        String userId = bodyMap.get("userId");
        String password = Optional.of(bodyMap.get("password")).orElse("");
        if (Objects.isNull(DataBase.findUserById(userId)) ||
                !password.equals(DataBase.findUserById(userId).getPassword())){
            return false;
        }
        return true;
    }
}
