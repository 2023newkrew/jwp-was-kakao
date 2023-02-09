package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Controller {

    static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public void process(HttpRequest request, HttpResponse response, DataOutputStream dos) throws IOException {
        String method = request.getRequestHeader().get("method").orElseThrow(IllegalArgumentException::new);
        if (method.equals("GET")) {
            doGet(request, response, dos);
        }
        if (method.equals("POST")) {
            doPost(request, response, dos);
        }
        doFinally(request, response, dos);
        sendResponse(response, dos);
    }

    protected void doGet(HttpRequest request, HttpResponse response, DataOutputStream dos) {}
    protected void doPost(HttpRequest request, HttpResponse response, DataOutputStream dos) {}
    protected void doFinally(HttpRequest request, HttpResponse response, DataOutputStream dos) {}

    private static void sendResponse(HttpResponse response, DataOutputStream dos) throws IOException {
        if (response.getResponseHeader() != null) {
            dos.writeBytes(response.getResponseHeader().getValue());
        }
        if (response.getResponseBody() != null) {
            dos.write(response.getResponseBody());
        }
        dos.flush();
    }
}
