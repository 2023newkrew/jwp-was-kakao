package webserver.handler.gethandler.impl;

import utils.FileIoUtils;
import webserver.constant.HttpStatus;
import webserver.handler.gethandler.GetRequestHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class TemplateResourceRequestHandler extends GetRequestHandler {

    private TemplateResourceRequestHandler() {
    }

    private static class TemplateResourceRequestHandlerHolder {
        public static final TemplateResourceRequestHandler INSTANCE = new TemplateResourceRequestHandler();
    }

    public static TemplateResourceRequestHandler getInstance() {
        return TemplateResourceRequestHandler.TemplateResourceRequestHandlerHolder.INSTANCE;
    }

    @Override
    public boolean canHandle(HttpRequest request) {
        String requestPath = request.getTarget()
                .getPath();
        return super.canHandle(request)
                && FileIoUtils.isFileExist("./templates" + requestPath);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String requestPath = request.getTarget()
                .getPath();
        byte[] body = FileIoUtils.loadFileFromClasspath("./templates" + requestPath);

        String contentType = FileIoUtils.getContentType("./templates" + requestPath);
        return new HttpResponse.Builder()
                .setStatus(HttpStatus.OK)
                .addHeader("Content-Type", contentType)
                .setBody(body)
                .build();
    }
}
