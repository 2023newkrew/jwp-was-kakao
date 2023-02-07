import app.controller.UserController;
import app.controller.ViewController;
import app.controller.support.UserUri;
import infra.RequestHandler;
import infra.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        ViewController viewController = new ViewController();
        UserController userController = new UserController();

        Router router = new Router(viewController);
        router.set(UserUri.CREATE.value(), userController);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                Thread thread = new Thread(new RequestHandler(connection, router));
                thread.start();
            }
        }
    }
}
