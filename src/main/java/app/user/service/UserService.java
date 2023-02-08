package app.user.service;

import app.user.User;
import app.user.dto.UserCreateDto;
import app.user.dto.UserResponseDto;
import java.util.List;

public interface UserService {

    User create(UserCreateDto userCreateDto);

    List<UserResponseDto> findAll();
}
