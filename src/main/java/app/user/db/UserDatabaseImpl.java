package app.user.db;

import auth.AuthUserDetails;
import app.excpetion.DuplicateException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class UserDatabaseImpl implements UserDatabase{

    private final Map<String, AuthUserDetails> database;

    public UserDatabaseImpl() {
        database = new HashMap<>();
    }

    @Override
    public AuthUserDetails save(AuthUserDetails entity) {
        if (Objects.nonNull(database.get(entity.getUserId()))) {
            throw new DuplicateException(UserDatabaseImpl.class.getSimpleName());
        }
        database.put(entity.getUserId(), entity);
        return entity;
    }

    @Override
    public Optional<AuthUserDetails> findById(String id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Collection<AuthUserDetails> findAll() {
        return database.values();
    }

    @Override
    public void deleteAll() {
        database.clear();
    }
}
