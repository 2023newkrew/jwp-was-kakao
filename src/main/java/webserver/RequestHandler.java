package webserver;

import webserver.controller.StaticFileController;
import webserver.enums.ContentType;
import webserver.exceptions.HandlerNotFoundException;
import webserver.exceptions.InvalidQueryParameterException;
import webserver.exceptions.InvalidRequestException;
import webserver.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import webserver.http.exceptionhandler.DefaultHttpExceptionHandlerMapping;
import webserver.http.exceptionhandler.HttpExceptionHandler;
import webserver.http.exceptionhandler.HttpExceptionHandlerMapping;
import webserver.http.requesthandler.DefaultHttpRequestHandlerMapping;
import webserver.http.requesthandler.HttpRequestHandler;
import webserver.http.requesthandler.HttpRequestHandlerMapping;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.FileIoUtils;
import webserver.utils.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final HttpRequestHandlerMapping httpRequestHandlerMapping;
    private final HttpExceptionHandlerMapping httpExceptionHandlerMapping;
    private final StaticFileController staticFileController;

    public RequestHandler(Socket connectionSocket, HttpRequestHandlerMapping requestMapping, HttpExceptionHandlerMapping exceptionMapping) {
        this.connection = connectionSocket;
        this.httpRequestHandlerMapping = requestMapping;
        this.httpExceptionHandlerMapping = exceptionMapping;
        this.staticFileController = StaticFileController.getInstance();
    }

    public RequestHandler(Socket connectionSocket) {
        this(connectionSocket, new DefaultHttpRequestHandlerMapping(), new DefaultHttpExceptionHandlerMapping());
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = IOUtils.parseRequest(in);
            HttpResponse response = handleHttpRequest(request);

            DataOutputStream dos = new DataOutputStream(out);
            response.writeToOutputStream(dos);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse handleHttpRequest(HttpRequest request) {
        try {
            HttpRequestHandler handler = findHandler(request);
            return handler.doHandle(request);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private HttpRequestHandler findHandler(HttpRequest request) throws Exception {
        if (FileIoUtils.isStaticFile(request)) {
            return staticFileController::staticFileGet;
        }

        return httpRequestHandlerMapping.findHandler(request);
    }

    private HttpResponse handleException(Exception e) {
        if (e instanceof HandlerNotFoundException || e instanceof ResourceNotFoundException) {
            return HttpResponse.status(HttpStatus.NOT_FOUND).contentType(ContentType.JSON).body(e.getMessage());
        }
        if (e instanceof InvalidRequestException || e instanceof URISyntaxException || e instanceof InvalidQueryParameterException) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).contentType(ContentType.JSON).body(e.getMessage());
        }
        if (e instanceof IOException) {
            return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(ContentType.JSON).body(e.getMessage());
        }

        try {
            HttpExceptionHandler handler = httpExceptionHandlerMapping.findHandler(e);
            return handler.doHandle(e);
        } catch (HandlerNotFoundException exception) {
            return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(ContentType.JSON).body(e.getMessage());
        }
    }
}
