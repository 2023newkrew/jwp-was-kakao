package webserver.service;

import db.DataBase;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import model.User;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import utils.LoginFailException;
import webserver.HttpCookie;
import webserver.request.HttpRequest;

@NoArgsConstructor
public class UserService {
    public static void createUser(HttpRequest httpRequest) {
        MultiValueMap<String,String> requestParams = getRequestParams(httpRequest);
        addUser(requestParams);
    }

    public static void login(HttpRequest httpRequest) {
        MultiValueMap<String,String> requestParams = getRequestParams(httpRequest);
        validateUser(requestParams);
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
        DataBase.addUser(user);
    }

    private static void validateUser(MultiValueMap<String, String> requestParams) {
        String userId = requestParams.getFirst("userId");
        String password = requestParams.getFirst("password");
        User user;
        try {
            user = DataBase.findUserById(userId);
        } catch (NullPointerException e) {
            throw new LoginFailException();
        }
        if (!user.comparePassword(password)) {
            throw new LoginFailException();
        }
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
