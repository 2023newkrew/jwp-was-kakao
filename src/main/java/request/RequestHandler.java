package request;

import controller.HandlerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringParser;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final StringParser stringParser;

    public RequestHandler(Socket connection, StringParser stringParser) {
        this.connection = connection;
        this.stringParser = stringParser;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = stringParser.getBufferReader(in);
            DataOutputStream dos = stringParser.getOutputStream(out);

            String requestInfo = br.readLine();
            RequestParams request = stringParser.getRequestParams(requestInfo);
            logger.debug("request method : {}, requestUrl : {}, httpVersion : {}, contentType: {}", request.getMethod(), request.getUrl(), request.getHttpVersion(), request.getContentType());
            HandlerMapper handlerMapper = new HandlerMapper(br, dos, stringParser);
            handlerMapper.methodMapping(request, requestInfo);

        } catch (IOException | URISyntaxException | RuntimeException e) {
            e.printStackTrace();
        }
    }
}
