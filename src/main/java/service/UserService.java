package service;

import db.DataBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.UserRequest;
import utils.ParamsParser;

import java.util.Map;

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
}
