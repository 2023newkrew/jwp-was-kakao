package auth.service;

import auth.AuthUserDetailsWithUuid;
import auth.dto.AuthLoginUserDto;
import java.util.Optional;

public interface AuthLoginService {
    Optional<AuthUserDetailsWithUuid> login(AuthLoginUserDto authLoginUserDto);
}
