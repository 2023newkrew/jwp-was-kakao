package auth.db;

import auth.AuthUserDetails;
import java.util.Optional;

public interface AuthLoginDatabase {
    Optional<AuthUserDetails> findById(String userId);
}
