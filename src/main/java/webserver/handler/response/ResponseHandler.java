package webserver.handler.response;

import model.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public interface ResponseHandler {
    void handle(HttpRequest httpRequest, OutputStream outputStream) throws IOException, URISyntaxException;

    byte[] generateBody(HttpRequest httpRequest) throws IOException, URISyntaxException;
}
