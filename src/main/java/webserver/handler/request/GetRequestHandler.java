package webserver.handler.request;

import model.HttpRequest;
import webserver.handler.response.GetResponseHandler;
import webserver.handler.response.ResponseHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class GetRequestHandler implements RequestMethodHandler {
    private static GetRequestHandler getRequestHandler = new GetRequestHandler();

    private GetRequestHandler() {}

    public static GetRequestHandler getInstance() {
        return getRequestHandler;
    }

    @Override
    public void handle(HttpRequest httpRequest, OutputStream outputStream) throws IOException, URISyntaxException {
        ResponseHandler responseHandler = GetResponseHandler.getInstance();

        responseHandler.handle(httpRequest, outputStream);
    }
}
