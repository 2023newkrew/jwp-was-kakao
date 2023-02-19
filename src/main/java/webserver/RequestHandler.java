package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.filter.Filters;
import webserver.handler.Handler;
import webserver.http.HttpRequestReader;
import webserver.support.GlobalExceptionHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final HandlerMappings handlerMappings;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final Filters filters;

    public RequestHandler(Socket connectionSocket, HandlerMappings handlerMappings, Filters filters) {
        this.connection = connectionSocket;
        this.handlerMappings = handlerMappings;
        this.filters = filters;
        this.globalExceptionHandler = new GlobalExceptionHandler(connection);
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (HttpRequestReader httpRequestReader = new HttpRequestReader(connection.getInputStream());
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            try {
                HttpRequest httpRequest = httpRequestReader.readHttpRequest();

                logger.debug("HTTP Request: {}", httpRequest);

                HttpResponse httpResponse = getHttpResponse();
                handle(httpRequest, httpResponse);

                logger.debug("HTTP Response : {} {} {}, headers = {}",
                        httpResponse.getVersion(),
                        httpResponse.getStatus().getCode(),
                        httpResponse.getStatus().getMessage(),
                        httpResponse.getHeaders());

                dos.write(httpResponse.toBytes());
                dos.flush();
            } catch (Exception e) {
                globalExceptionHandler.handle(e);
            }
        } catch (IOException e) {
            logger.error("Socket IOException! - {}", e.getMessage());
        }
    }

    private HttpResponse getHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setVersion("HTTP/1.1");
        return httpResponse;
    }

    private void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        Handler handler = handlerMappings.findHandler(httpRequest);
        filters.doFilterAndHandle(httpRequest, httpResponse, handler);
    }
}
