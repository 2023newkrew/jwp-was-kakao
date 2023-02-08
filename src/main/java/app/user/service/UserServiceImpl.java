package app.user.service;

import app.user.User;
import app.user.dao.UserDao;
import app.user.dto.UserCreateDto;
import app.user.dto.UserResponseDto;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;

    }

    @Override
    public User create(UserCreateDto userCreateDto) {
        return userDao.save(userCreateDto.toEntity());
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userDao.findAll().stream().map(UserResponseDto::of).collect(Collectors.toList());
    }
}
