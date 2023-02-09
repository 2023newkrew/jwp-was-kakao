package framework;

import framework.controller.Controller;
import framework.controller.ExceptionController;
import framework.request.Request;
import framework.request.RequestParser;
import framework.requestmapper.ExceptionHandlerMapper;
import framework.requestmapper.HandlerMapper;
import framework.requestmapper.ResourceMapper;
import framework.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.net.Socket;

import static framework.utils.IOUtils.writeResponse;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dataOutputStream = new DataOutputStream(out);

            Request request = RequestParser.getRequestFrom(bufferedReader);
            String uri = request.getUri();
            logger.debug("{} {}", request.getMethod(), request.getUri());

            try {
                Controller handler;
                Response response;
                if ((handler = HandlerMapper.getInstance().findHandler(uri)) != null) {
                    response = HandlerMapper.getInstance().handle(request, handler);
                } else {
                    response = ResourceMapper.getInstance().handle(uri);
                }
                writeResponse(dataOutputStream, response.getBytes());
            } catch (Exception e) {
                ExceptionController handler;
                Response response;
                if ((handler = ExceptionHandlerMapper.getInstance().findHandler(e)) != null) {
                    response = ExceptionHandlerMapper.getInstance().handle(e, handler);
                } else {
                    response = Response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("500 Internal Server Error")
                            .build();
                }
                writeResponse(dataOutputStream, response.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
