package app.user.dao;

import app.user.User;
import java.util.List;

public interface UserDao {
    User save(User user);

    List<User> findAll();
}
