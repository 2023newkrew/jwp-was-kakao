package webserver;

import webserver.handler.Handlers;
import webserver.handler.ResourceHandler;
import webserver.handler.controller.RootController;
import webserver.handler.controller.UserController;
import webserver.handler.resolver.Resolvers;
import webserver.handler.resolver.resource.ResourceResolver;
import webserver.handler.resolver.view.ViewResolver;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private static final int DEFAULT_PORT = 8080;

    public static final Resolvers resolvers = new Resolvers(
            new ResourceResolver(),
            new ViewResolver()
    );

    public static final Handlers handlers = new Handlers(
            new RootController(),
            new UserController(),
            new ResourceHandler(resolvers)
    );


    public static void main(String... args) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        }
        else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection, handlers);
                Thread thread = new Thread(requestHandler);
                thread.start();
            }
        }
    }
}
