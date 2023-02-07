package webserver.handler;

import org.springframework.http.HttpStatus;
import webserver.request.HttpRequest;
import webserver.HttpResponse;

public class BaseHandler implements Handler {

    @Override
    public HttpResponse applyRequest(HttpRequest request) {
        byte[] body = "Hello world".getBytes();
        HttpResponse response = new HttpResponse(HttpStatus.OK, body);
        response.setContentType("text/html");
        response.setContentLength(body.length);
        return response;
    }
}
