package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import http.HttpRequest;
import webserver.http.HttpRequestReader;
import http.HttpResponse;
import http.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Map<String, Handler> urlHandlerMapping;
    private final List<Handler> defaultHandlerMapping;

    public RequestHandler(Socket connectionSocket, Map<String, Handler> urlHandlerMapping, List<Handler> defaultHandlerMapping) {
        this.connection = connectionSocket;
        this.urlHandlerMapping = urlHandlerMapping;
        this.defaultHandlerMapping = defaultHandlerMapping;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (HttpRequestReader httpRequestReader = new HttpRequestReader(connection.getInputStream());
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {

            HttpRequest httpRequest = httpRequestReader.readHttpRequest();

            if (httpRequest == null) {
                logger.debug("HTTP Request is null! Connection closed.");
                connection.close();
                return;
            }

            logger.debug("HTTP Request: {}", httpRequest);

            HttpResponse httpResponse = null;
            Handler urlHandler = urlHandlerMapping.get(httpRequest.getURL());
            if (urlHandler != null) {
                httpResponse = urlHandler.handle(httpRequest);
            } else {
                for (Handler handler : defaultHandlerMapping) {
                    try {
                        httpResponse = handler.handle(httpRequest);
                        break;
                    } catch (Exception ignored) {
                    }
                }
                if (httpResponse == null) {
                    httpResponse = HttpResponse.HttpResponseBuilder.aHttpResponse()
                            .withStatus(HttpStatus.BAD_REQUEST)
                            .withVersion("HTTP/1.1")
                            .build();
                }
            }

            logger.debug("HTTP Response: {}", httpResponse);

            dos.write(httpResponse.toBytes());
            dos.flush();

            connection.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (IOException ignored) {}
        }
    }
}
