import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ForkJoinPoolUtils;
import utils.UUIDGenerator;
import web.RequestHandler;
import web.controller.*;

import java.net.ServerSocket;
import java.net.Socket;

import static web.config.SessionConfig.MEMORY_SESSION_MANAGER;

public class WebServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(WebServerApplication.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = getPort(args);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);
            HandlerMapping handlerMapping = createHandlerMapping();

            Socket connection;
            while ((connection = listenSocket.accept()).isConnected()) {
                ForkJoinPoolUtils.execute(new RequestHandler(connection, handlerMapping));
            }
        }
    }

    private static HandlerMapping createHandlerMapping() {
        return HandlerMapping.of(
                new DefaultController(),
                new PostSignInController(),
                new PostLoginController(new UUIDGenerator(), MEMORY_SESSION_MANAGER.getSessionManager()),
                new GetUserListController(MEMORY_SESSION_MANAGER.getSessionManager()),
                new GetResourceController()
        );
    }

    private static int getPort(String[] args) {
        if (args == null || args.length == 0) {
            return DEFAULT_PORT;
        }

        return Integer.parseInt(args[0]);
    }

}
