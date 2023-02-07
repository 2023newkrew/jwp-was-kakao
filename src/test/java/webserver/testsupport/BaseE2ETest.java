package webserver.testsupport;

import webserver.RequestHandler;
import webserver.http.exceptionhandler.HttpExceptionHandlerMapping;
import webserver.http.requesthandler.HttpRequestHandlerMapping;

public abstract class BaseE2ETest {
    private final HttpRequestHandlerMapping httpRequestHandlerMapping;
    private final HttpExceptionHandlerMapping httpExceptionHandlerMapping;

    protected BaseE2ETest(HttpRequestHandlerMapping httpRequestHandlerMapping, HttpExceptionHandlerMapping httpExceptionHandlerMapping) {
        this.httpRequestHandlerMapping = httpRequestHandlerMapping;
        this.httpExceptionHandlerMapping = httpExceptionHandlerMapping;
    }

    protected String getSocketOutputFromWebServer(String socketInput) {
        StubSocket socket = new StubSocket(socketInput);
        RequestHandler handler = new RequestHandler(socket, httpRequestHandlerMapping, httpExceptionHandlerMapping);

        handler.run();
        return socket.output();
    }
}
