package webserver.handler.gethandler.impl;

import utils.FileIoUtils;
import webserver.constant.HttpHeaderProperties;
import webserver.constant.HttpStatus;
import webserver.handler.gethandler.GetRequestHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class TemplateResourceRequestHandler extends GetRequestHandler {

    public static final String TEMPLATES_FILE_PREFIX = "./templates";

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
                && FileIoUtils.isFileExist(TEMPLATES_FILE_PREFIX + requestPath);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String requestPath = request.getTarget()
                .getPath();
        byte[] body = FileIoUtils.loadFileFromClasspath(TEMPLATES_FILE_PREFIX + requestPath);

        String contentType = FileIoUtils.getContentType(TEMPLATES_FILE_PREFIX + requestPath);
        return new HttpResponse.Builder()
                .setStatus(HttpStatus.OK)
                .addHeader(HttpHeaderProperties.CONTENT_TYPE.getKey(), contentType)
                .setBody(body)
                .build();
    }
}
