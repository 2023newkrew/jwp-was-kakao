package webserver.handler.request;

import model.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public interface RequestMethodHandler {
    void handle(HttpRequest httpRequest, OutputStream outputStream) throws IOException, URISyntaxException;
}
