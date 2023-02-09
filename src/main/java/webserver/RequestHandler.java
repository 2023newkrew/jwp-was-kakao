package webserver;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;
import request.RequestParser;
import requestmapper.HandlerMapper;
import requestmapper.ResourceMapper;
import response.Response;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

import static utils.IOUtils.writeResponse;

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

            Controller handler;
            Response response;
            if ((handler = HandlerMapper.getInstance().findHandler(uri)) != null) {
                response = HandlerMapper.getInstance().handle(request, handler);
            } else {
                response = ResourceMapper.getInstance().handle(uri);
            }
            writeResponse(dataOutputStream, response.toString());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
