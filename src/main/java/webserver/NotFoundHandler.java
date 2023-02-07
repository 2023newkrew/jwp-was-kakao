package webserver;

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
    public HttpResponse handle(HttpRequest request) {
        return new HttpResponse.Builder()
                .setStatus(HttpStatus.NOT_FOUND)
                .build();
    }
}
