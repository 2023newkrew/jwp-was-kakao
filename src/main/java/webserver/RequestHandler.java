package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;
import request.RequestParser;
import response.Response;
import requestmapper.HandlerMapper;
import requestmapper.ResourceMapper;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Map;

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
            Map<String, String> requestParameter = request.getRequestParams();
            String uri = request.getUri();

            if (HandlerMapper.INSTANCE.isHandleAvailable(uri)) {
                Response response = HandlerMapper.INSTANCE.handle(uri, requestParameter);
                writeResponse(dataOutputStream, response.toString());
                return;
            }

            Response response = ResourceMapper.INSTANCE.handle(uri);
            writeResponse(dataOutputStream, response.toString());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }
}
