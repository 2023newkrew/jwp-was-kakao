package webserver.service;

import db.DataBase;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import model.User;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import utils.ExistUserException;
import utils.LoginFailException;
import webserver.request.HttpRequest;

@NoArgsConstructor
public class UserService {
    public static void registerUser(HttpRequest httpRequest) {
        MultiValueMap<String,String> requestParams = getRequestParams(httpRequest);
        addUser(requestParams);
    }

    public static User login(HttpRequest httpRequest) {
        MultiValueMap<String,String> requestParams = getRequestParams(httpRequest);
        return validateAndGetUser(requestParams);
    }

    public static List<User> getUserList() {
        return new ArrayList<>(DataBase.findAll());
    }

    private static void addUser(MultiValueMap<String, String> requestParams) {
        String userId = requestParams.getFirst("userId");
        String password = requestParams.getFirst("password");
        String name = requestParams.getFirst("name");
        String email = requestParams.getFirst("email");
        User user = User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();
        if (DataBase.findUserById(userId) != null){
            throw new ExistUserException();
        }
        DataBase.addUser(user);
    }

    private static User validateAndGetUser(MultiValueMap<String, String> requestParams) {
        String userId = requestParams.getFirst("userId");
        String password = requestParams.getFirst("password");
        User user = DataBase.findUserById(userId);
        if (user == null || !user.comparePassword(password)) {
            throw new LoginFailException();
        }
        return user;
    }

    private static MultiValueMap<String,String> getRequestParams(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        String requestBody = httpRequest.getBody();
        requestBody = URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
        return UriComponentsBuilder.fromUriString(path)
                .query(requestBody)
                .build()
                .getQueryParams();
    }
}
