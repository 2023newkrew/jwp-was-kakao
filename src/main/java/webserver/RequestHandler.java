package webserver;

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

            if (HandlerMapper.getInstance().isHandleAvailable(uri)) {
                Response response = HandlerMapper.getInstance().handle(request);
                writeResponse(dataOutputStream, response.toString());
                return;
            }

            Response response = ResourceMapper.getInstance().handle(uri);
            writeResponse(dataOutputStream, response.toString());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }
}
