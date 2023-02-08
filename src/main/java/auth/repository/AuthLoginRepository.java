package auth.repository;

import auth.AuthUserDetailsWithUuid;
import java.util.Optional;

public interface AuthLoginRepository {
    Optional<AuthUserDetailsWithUuid> findByUserId(String userId);
}
