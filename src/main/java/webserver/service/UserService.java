package webserver.service;

import lombok.RequiredArgsConstructor;
import model.LoginRequest;
import model.User;
import model.UserRequest;
import repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void create(UserRequest userRequest) {
        User user = userRequest.toEntity();
        userRepository.save(user);
    }

    public Optional<User> login(LoginRequest loginRequest) {
        return userRepository.findUserById(loginRequest.getUserId())
                .filter(user -> user.isPasswordMatch(loginRequest.getPassword()));
    }
}
