package webserver;

import utils.FileIoUtils;

public class StaticResourceRequestHandler extends GetRequestHandler {

    private StaticResourceRequestHandler() {
    }

    private static class TemplateResourceRequestHandlerHolder {
        public static final StaticResourceRequestHandler INSTANCE = new StaticResourceRequestHandler();
    }

    public static StaticResourceRequestHandler getInstance() {
        return StaticResourceRequestHandler.TemplateResourceRequestHandlerHolder.INSTANCE;
    }

    @Override
    public boolean canHandle(HttpRequest request) {
        String requestPath = request.getTarget()
                .getPath();
        return super.canHandle(request)
                && FileIoUtils.isFileExist("./static" + requestPath);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String requestPath = request.getTarget()
                .getPath();
        byte[] body = FileIoUtils.loadFileFromClasspath("./static" + requestPath);

        String contentType = FileIoUtils.getContentType("./static" + requestPath);
        return new HttpResponse.Builder()
                .setStatus(HttpStatus.OK)
                .addHeader("Content-Type", contentType)
                .setBody(body)
                .build();
    }
}
