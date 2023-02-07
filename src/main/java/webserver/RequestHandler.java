package webserver;

import lombok.extern.slf4j.Slf4j;
import webserver.handler.HttpRequestHandler;
import webserver.handler.resolver.HandlerResolver;
import webserver.request.HttpRequest;

import java.io.*;
import java.net.Socket;

@Slf4j
public class RequestHandler implements Runnable {

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
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
            log.error(e.getMessage());
        }
    }
}
