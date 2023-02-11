package webserver.service;

import lombok.RequiredArgsConstructor;
import webserver.dto.LoginRequest;
import webserver.model.User;
import webserver.dto.UserRequest;
import webserver.repository.UserRepository;

import java.util.Collection;
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

    public Collection<User> findAll() {
        return userRepository.findAll();
    }
}
