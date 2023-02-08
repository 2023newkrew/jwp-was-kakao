package repository;

import model.User;

public interface SessionRepository {
    void put(String key, User user);

    User get(String key);

    boolean containsKey(String uuid);
}
