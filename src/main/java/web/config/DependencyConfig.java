package web.config;

import utils.SessionIdGenerator;
import utils.UUIDGenerator;
import web.controller.*;
import web.infra.MemorySessionManager;
import web.infra.SessionManager;
import web.validator.LoginValidator;

public class DependencyConfig {

    private final static SessionManager sessionManager = new MemorySessionManager();
    private final static SessionIdGenerator sessionIdGenerator = new UUIDGenerator();
    private final static LoginValidator loginValidator = new LoginValidator(sessionManager);
    private final static HandlerMapping handlerMapping = HandlerMapping.of(
            new DefaultController(),
            new PostSignInController(),
            new PostLoginController(sessionIdGenerator, sessionManager),
            new GetUserListController(loginValidator),
            new GetResourceController(loginValidator)
    );

    public static HandlerMapping getHandlerMapping() {
        return handlerMapping;
    }
}
