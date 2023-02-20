package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.handler.HandlerMapper;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestParser;
import webserver.response.HttpResponse;
import webserver.security.SecurityHandler;
import webserver.security.SessionManager;
import webserver.utils.ResponseUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final HandlerMapper handlerMapper;
    private final SecurityHandler securityHandler;

    public RequestHandler(Socket connectionSocket, HandlerMapper handlerMapper, SecurityHandler securityHandler) {
        this.connection = connectionSocket;
        this.handlerMapper = handlerMapper;
        this.securityHandler = securityHandler;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            try {
                final HttpRequest request = HttpRequestParser.parse(in);
                final HttpResponse response = HttpResponse.of(new DataOutputStream(out));
                final Controller controller = handlerMapper.getController(request);

                if (securityHandler.isNeedAuthentication(request.getUri().getPath()) && securityHandler.isNotAuthenticated(request)) {
                    ResponseUtil.response302(response, "/user/login.html");
                    response.send();
                    return;
                }

                if (isUserTryLoginAgain(request)) {
                    ResponseUtil.response302(response, "/index.html");
                    response.send();
                    return;
                }

                controller.service(request, response);
                response.send();
            } catch (RuntimeException e) {
                final HttpResponse response = HttpResponse.of(new DataOutputStream(out));
                ResponseUtil.response400(response, e.getMessage());
                response.send();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static boolean isUserTryLoginAgain(HttpRequest request) {
        return request.getUri().getPath().equals("/user/login.html") &&
                request.getCookie("JSESSIONID").isPresent() &&
                SessionManager.getInstance().findSession(request.getCookie("JSESSIONID").get()).isPresent();
    }
}
