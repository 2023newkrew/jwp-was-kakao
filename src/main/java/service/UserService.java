package service;

import db.DataBase;
import lombok.RequiredArgsConstructor;
import model.UserRequest;

import java.util.Map;

@RequiredArgsConstructor
public class UserService {
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
