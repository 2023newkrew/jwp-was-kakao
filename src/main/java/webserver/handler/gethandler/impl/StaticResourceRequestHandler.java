package webserver.handler.gethandler.impl;

import utils.FileIoUtils;
import webserver.constant.HttpHeaderProperties;
import webserver.constant.HttpStatus;
import webserver.handler.gethandler.GetRequestHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class StaticResourceRequestHandler extends GetRequestHandler {

    public static final String STATIC_FILES_PREFIX = "./static";

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
                && FileIoUtils.isFileExist(STATIC_FILES_PREFIX + requestPath);
    }

    @Override
    public void handle(HttpRequest request, HttpResponse.Builder responseBuilder) {
        String requestPath = request.getTarget()
                .getPath();
        byte[] body = FileIoUtils.loadFileFromClasspath(STATIC_FILES_PREFIX + requestPath);

        String contentType = FileIoUtils.getContentType(STATIC_FILES_PREFIX + requestPath);
        responseBuilder.setStatus(HttpStatus.OK)
                .addHeader(HttpHeaderProperties.CONTENT_TYPE.getKey(), contentType)
                .setBody(body)
                .build();
    }
}
