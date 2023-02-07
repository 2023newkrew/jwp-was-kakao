package webserver;

import db.DataBase;
import lombok.NoArgsConstructor;
import model.User;
import org.springframework.util.MultiValueMap;

@NoArgsConstructor
public class UserService {
    public void addUser(MultiValueMap<String, String> requestParams) {
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
}
