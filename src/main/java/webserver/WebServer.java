package webserver;

import container.config.WebMvcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    private final int port;
    private final ResourceResolver resourceResolver;
    private final HandlerMapping handlerMapping;
    public WebServer(int port) {
        this.port = port;
        this.handlerMapping = new HandlerMapping();
        this.resourceResolver = new ResourceResolver();
    }

    public void start() throws Exception {
        init();
        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                        connection.getPort());

                ;
                Thread thread = new Thread(new RequestHandler(connection, resourceResolver, handlerMapping));
                thread.start();
            }
        }
    }

    private void init() {
        loadConfig();
        mappingHandler();
    }

    private void loadConfig() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebMvcConfig.class);

        applicationContext.getBeansOfType(WebMvcConfig.class)
                .values()
                .forEach(config -> config.addResourceHandlers(resourceResolver));
    }

    private void mappingHandler() {

    }
}
