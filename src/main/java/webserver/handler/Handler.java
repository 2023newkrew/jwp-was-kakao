package webserver.handler;

import webserver.request.Request;
import webserver.response.Response;

public interface Handler {
    
    boolean canHandle(Request request);

    Response handle(Request request);
}
