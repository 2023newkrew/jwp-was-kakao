package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.handler.HandlerMapper;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestParser;
import webserver.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final HandlerMapper handlerMapper;

    public RequestHandler(Socket connectionSocket, HandlerMapper handlerMapper) {
        this.connection = connectionSocket;
        this.handlerMapper = handlerMapper;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = HttpRequestParser.parse(in);
            Controller controller = handlerMapper.getController(request);
            HttpResponse response = HttpResponse.of(new DataOutputStream(out), controller.getSuccessCode());

            controller.service(request, response);

            response.send();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
