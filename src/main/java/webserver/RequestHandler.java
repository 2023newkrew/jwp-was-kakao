package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import http.HttpRequest;
import webserver.handler.StaticResourceRequestHandler;
import webserver.handler.UrlMappingHandler;
import webserver.http.HttpRequestReader;
import http.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Map<String, UrlMappingHandler> urlMappingHandlerMappings;
    private final Handler defaultHandler;

    public RequestHandler(Socket connectionSocket,
                          Map<String, UrlMappingHandler> urlMappingHandlerMappings,
                          Handler defaultHandler) {
        this.connection = connectionSocket;
        this.urlMappingHandlerMappings = urlMappingHandlerMappings;
        this.defaultHandler = defaultHandler;
    }

    public RequestHandler(Socket connection, Map<String, UrlMappingHandler> urlMappingHandlerMappings) {
        this(connection, urlMappingHandlerMappings, new StaticResourceRequestHandler());
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

            HttpResponse httpResponse = handle(httpRequest);

            logger.debug("HTTP Response: {}", httpResponse);

            dos.write(httpResponse.toBytes());
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (IOException ignored) {}
        }
    }

    private HttpResponse handle(HttpRequest httpRequest) {
        Handler handler = findUrlHandler(httpRequest);
        if (handler != null) {
            return handler.handle(httpRequest);
        }
        return defaultHandler.handle(httpRequest);
    }

    private Handler findUrlHandler(HttpRequest httpRequest) {
        return urlMappingHandlerMappings.keySet().stream()
                .filter(urlMapping -> Pattern.compile(urlMapping).matcher(httpRequest.getURL()).matches())
                .map(urlMappingHandlerMappings::get)
                .filter(handler -> handler.support(httpRequest))
                .findFirst()
                .orElse(null);
    }
}
