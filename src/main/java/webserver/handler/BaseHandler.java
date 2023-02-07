package webserver.handler;

import webserver.request.Request;
import webserver.response.Response;

public class BaseHandler implements Handler {
    private static final String BASE_STRING = "Hello world";
    @Override
    public Response apply(Request request) {
        return Response.ok(BASE_STRING.getBytes(), request.getRequestFileType());
    }
}
