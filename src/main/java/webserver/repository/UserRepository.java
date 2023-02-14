package webserver.repository;

import webserver.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findUserById(String userId);

    Collection<User> findAll();
}
