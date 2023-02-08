package auth.dao;

import auth.AuthUserDetailsWithUuid;
import java.util.Optional;

public interface AuthLoginDao {
    Optional<AuthUserDetailsWithUuid> findByUserId(String userId);
}
