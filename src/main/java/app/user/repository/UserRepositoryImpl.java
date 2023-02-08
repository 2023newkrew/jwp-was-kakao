package app.user.repository;

import auth.AuthUserDetails;
import app.user.User;
import infra.db.Database;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {

    private final Database<AuthUserDetails> userDatabase;

    public UserRepositoryImpl(Database<AuthUserDetails> userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public User save(User user) {
        AuthUserDetails userDetail = userDatabase.save(user);
        return User.builder()
                .userId(userDetail.getUserId())
                .name(userDetail.getName())
                .email(userDetail.getEmail())
                .password(userDetail.getPassword())
                .build();
    }

    @Override
    public List<User> findAll() {
        return userDatabase.findAll().stream().map(userDetail -> User.builder()
                .userId(userDetail.getUserId())
                .name(userDetail.getName())
                .email(userDetail.getEmail())
                .password(userDetail.getPassword())
                .build()).collect(Collectors.toList());
    }
}
