package webserver.handler;

import webserver.constant.HttpStatus;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class NotFoundHandler implements HttpRequestHandler {

    private NotFoundHandler() {
    }

    private static class NotFoundHandlerHolder {
        public static final NotFoundHandler INSTANCE = new NotFoundHandler();
    }

    public static NotFoundHandler getInstance() {
        return NotFoundHandler.NotFoundHandlerHolder.INSTANCE;
    }


    @Override
    public boolean canHandle(HttpRequest request) {
        return true;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse.Builder responseBuilder) {
        responseBuilder.setStatus(HttpStatus.NOT_FOUND)
                .build();
    }
}
