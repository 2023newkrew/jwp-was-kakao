package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import webserver.handler.StaticResourceRequestHandler;
import webserver.handler.UrlMappingHandler;
import webserver.http.HttpRequestReader;
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
    private final HandlerMappings handlerMappings;
    private final GlobalExceptionHandler globalExceptionHandler;

    public RequestHandler(Socket connectionSocket, HandlerMappings handlerMappings) {
        this.connection = connectionSocket;
        this.handlerMappings = handlerMappings;
        this.globalExceptionHandler = new GlobalExceptionHandler(connection);
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
        Handler handler = handlerMappings.findHandler(httpRequest);
        return handler.handle(httpRequest);
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
