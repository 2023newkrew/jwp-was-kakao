package app.user.repository;

import app.user.User;
import java.util.List;

public interface UserRepository {
    User save(User user);

    List<User> findAll();
}
