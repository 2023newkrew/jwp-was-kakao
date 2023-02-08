package support;

import web.RequestHandler;
import web.controller.*;
import web.infra.MemorySessionManager;

import java.net.Socket;

import static web.config.SessionConfig.MEMORY_SESSION_MANAGER;

public class RequestHandlerFactory {

    private static final HandlerMapping handlerMapping = HandlerMapping.of(
            new DefaultController(),
            new PostSignInController(),
            new PostLoginController(() -> "UUID", new MemorySessionManager()),
            new GetUserListController(MEMORY_SESSION_MANAGER.getInstance()),
            new GetResourceController()
    );

    public static RequestHandler create(Socket connectionSocket) {
        return new RequestHandler(connectionSocket, handlerMapping);
    }

}
