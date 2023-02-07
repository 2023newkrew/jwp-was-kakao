package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import http.HttpRequest;
import webserver.handler.StaticResourceRequestHandler;
import webserver.handler.UrlMappingHandler;
import webserver.http.HttpRequestReader;
import http.HttpResponse;
import webserver.support.GlobalExceptionHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Map<String, UrlMappingHandler> urlMappingHandlerMappings;
    private final Handler defaultHandler;
    private final GlobalExceptionHandler globalExceptionHandler;

    public RequestHandler(Socket connectionSocket,
                          Map<String, UrlMappingHandler> urlMappingHandlerMappings,
                          Handler defaultHandler) {
        this.connection = connectionSocket;
        this.urlMappingHandlerMappings = urlMappingHandlerMappings;
        this.defaultHandler = defaultHandler;
        this.globalExceptionHandler = new GlobalExceptionHandler(connection);
    }

    public RequestHandler(Socket connection, Map<String, UrlMappingHandler> urlMappingHandlerMappings) {
        this(connection, urlMappingHandlerMappings, new StaticResourceRequestHandler());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        HttpRequestReader httpRequestReader = null;
        DataOutputStream dos = null;
        try {
            httpRequestReader = new HttpRequestReader(connection.getInputStream());
            dos = new DataOutputStream(connection.getOutputStream());

            HttpRequest httpRequest = httpRequestReader.readHttpRequest();

            if (httpRequest == null) {
                logger.debug("HTTP Request is null! Connection closed.");
                close(httpRequestReader, dos, connection);
                return;
            }

            logger.debug("HTTP Request: {}", httpRequest);

            HttpResponse httpResponse = handle(httpRequest);

            logger.debug("HTTP Response Status Line: {} {} {}",
                    httpResponse.getVersion(),
                    httpResponse.getStatus().getCode(),
                    httpResponse.getStatus().getMessage());

            dos.write(httpResponse.toBytes());
            dos.flush();
        } catch (Exception e) {
            logger.error("Exception! - {}", e.getMessage());
            globalExceptionHandler.handle(e);
        } finally {
            close(httpRequestReader, dos, connection);
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

    private void close(HttpRequestReader reader, OutputStream os, Socket connection) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (os != null) {
                os.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException ignored) {
            throw new IllegalStateException();
        }
    }
}
