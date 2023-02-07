import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.RequestHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(WebServerApplication.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = getPort(args);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            Socket connection;
            while ((connection = listenSocket.accept()).isConnected()) {
                new Thread(new RequestHandler(connection)).start();
            }
        }
    }

    private static int getPort(String[] args) {
        if (args == null || args.length == 0) {
            return DEFAULT_PORT;
        }

        return Integer.parseInt(args[0]);
    }

}
