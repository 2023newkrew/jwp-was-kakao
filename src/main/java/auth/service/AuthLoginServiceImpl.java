package auth.service;

import auth.AuthUserDetails;
import auth.AuthUserDetailsWithUuid;
import auth.dao.AuthLoginDao;
import auth.dto.AuthLoginUserDto;
import infra.session.Session;
import infra.session.SessionManager;
import java.util.Optional;

public class AuthLoginServiceImpl implements AuthLoginService {

    private final AuthLoginDao authLoginDao;
    private final SessionManager sessionManager;

    public AuthLoginServiceImpl(AuthLoginDao authLoginDao, SessionManager sessionManager) {
        this.authLoginDao = authLoginDao;
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<AuthUserDetailsWithUuid> login(AuthLoginUserDto authLoginUserDto) {
        Optional<AuthUserDetailsWithUuid> authUserDetails = authLoginDao.findByUserId(authLoginUserDto.getUserId());
        if (authUserDetails.isPresent() && isValid(authUserDetails.get(), authLoginUserDto)) {
            AuthUserDetailsWithUuid presentDetails = authUserDetails.get();
            makeSession(presentDetails);
            return authUserDetails;
        }
        return Optional.empty();
    }

    private boolean isValid(AuthUserDetails authUserDetails, AuthLoginUserDto authLoginUserDto) {
        return authUserDetails.getUserId().equals(authLoginUserDto.getUserId()) &&
                authUserDetails.getPassword().equals(authLoginUserDto.getPassword());

    }

    private void makeSession(AuthUserDetailsWithUuid userDetails) {
        Session session = new Session(userDetails.getUuid());
        sessionManager.add(session);
        sessionManager.findSession(userDetails.getUuid()).setAttribute(session.getId(), userDetails);
    }
}
