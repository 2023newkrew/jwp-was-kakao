package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.HttpRequestHandler;
import webserver.handler.resolver.HandlerResolver;
import webserver.request.HttpRequest;

import java.io.*;
import java.net.Socket;

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
            DataOutputStream dos = new DataOutputStream(out);
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            HttpRequest httpRequest = new HttpRequest(bufferedReader);
            HttpRequestHandler handler = HandlerResolver.getInstance()
                    .resolve(httpRequest);
            handler.handle(httpRequest)
                    .send(dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
