package controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import service.UserService;
import was.annotation.Controller;
import was.annotation.Mapping;
import was.annotation.RequestBody;
import was.annotation.RequestMethod;
import was.domain.response.Response;
import was.domain.response.StatusCode;
import was.domain.response.Version;

import java.util.Optional;

@Controller
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserController {
    @RequestBody
    @Mapping(method = RequestMethod.POST, path="/user/create")
    public static Optional<Response> createUser(String body){
        UserService.createUser(body);
        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.FOUND)
                .location("/index.html")
                .build());
    }
}
