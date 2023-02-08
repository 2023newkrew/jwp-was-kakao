package webserver.handler;

import webserver.http.request.Request;
import webserver.http.response.Response;

public interface Handler {

    boolean canHandle(Request request);

    Response handle(Request request);
}
