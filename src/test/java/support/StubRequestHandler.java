package support;

import web.RequestHandler;
import web.controller.*;

import java.net.Socket;

public class StubRequestHandler extends RequestHandler {

    public StubRequestHandler(Socket connectionSocket) {
        super(connectionSocket, createHandlerMapping());
    }

    private static HandlerMapping createHandlerMapping() {
        return HandlerMapping.of(
                new DefaultController(),
                new GetResourceController(),
                new PostSignInController(),
                new PostLoginController(() -> "UUID")
        );
    }

}
