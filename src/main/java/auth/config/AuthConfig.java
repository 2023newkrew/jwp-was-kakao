package auth.config;

import auth.controller.AuthLoginController;
import auth.dao.AuthLoginDao;
import auth.dao.AuthLoginDaoImpl;
import auth.db.AuthLoginDatabase;
import auth.filter.JSessionAuthUserDetailsFilter;
import auth.repository.AuthLoginRepository;
import auth.repository.AuthLoginRepositoryImpl;
import auth.service.AuthLoginService;
import auth.service.AuthLoginServiceImpl;
import infra.session.SessionManager;

public class AuthConfig {
    private final AuthLoginController authLoginController;
    private final AuthLoginService authLoginService;
    private final AuthLoginDao authLoginDao;
    private final AuthLoginRepository authLoginRepository;
    private final AuthLoginDatabase authLoginDatabase;
    private final JSessionAuthUserDetailsFilter myFilter;

    protected AuthConfig(AuthLoginDatabase authLoginDatabase, SessionManager sessionManager) {
        this.authLoginDatabase = authLoginDatabase;
        this.authLoginRepository = new AuthLoginRepositoryImpl(this.authLoginDatabase);
        this.authLoginDao = new AuthLoginDaoImpl(this.authLoginRepository);
        this.authLoginService = new AuthLoginServiceImpl(this.authLoginDao,sessionManager);
        this.authLoginController = new AuthLoginController(this.authLoginService);
        this.myFilter = new JSessionAuthUserDetailsFilter(sessionManager);
    }

    public JSessionAuthUserDetailsFilter getMyFilter() {
        return myFilter;
    }

    public AuthLoginDatabase getAuthLoginDatabase() {
        return authLoginDatabase;
    }


    public AuthLoginController getAuthLoginController() {
        return authLoginController;
    }

    public AuthLoginService getAuthLoginService() {
        return authLoginService;
    }

    public AuthLoginDao getAuthLoginDao() {
        return authLoginDao;
    }

    public AuthLoginRepository getAuthLoginRepository() {
        return authLoginRepository;
    }

    public AuthLoginDatabase getAuthLoginUserDatabase() {
        return authLoginDatabase;
    }
}
