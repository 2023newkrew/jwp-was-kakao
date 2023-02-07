package webserver.handler;

import model.User;
import repository.MemoryUserRepository;
import repository.UserRepository;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Map;

public class CreateUserHandler implements Handler {

    private final String REDIRECT_LOCATION = "/index.html";
    private final UserRepository userRepository = new MemoryUserRepository();

    @Override
    public Response apply(Request request) {
        Map<String, String> queryStringMap = request.getRequestBodyAsQueryString();
        User user = User.builder()
                .userId(queryStringMap.get("userId"))
                .password(queryStringMap.get("password"))
                .name(queryStringMap.get("name"))
                .email(queryStringMap.get("email"))
                .build();
        userRepository.save(user);
        return Response.found(new byte[0], request.getRequestFileType(), REDIRECT_LOCATION);
    }
}
