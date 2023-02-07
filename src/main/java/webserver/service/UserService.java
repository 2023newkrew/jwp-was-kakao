package webserver.service;

import lombok.RequiredArgsConstructor;
import model.User;
import model.UserRequest;
import repository.UserRepository;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void create(UserRequest userRequest) {
        User user = userRequest.toEntity();
        userRepository.save(user);
    }
}
