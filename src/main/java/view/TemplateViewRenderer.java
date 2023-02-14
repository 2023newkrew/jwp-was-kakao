package view;

import utils.FileIoUtils;
import webserver.HttpResponse;

import java.io.IOException;

public class TemplateViewRenderer implements ViewRenderer {
    private static final String TEMPLATE_PATH_PREFIX = "./templates";
    private final String url;

    public TemplateViewRenderer(String url) {
        this.url = url;
    }

    @Override
    public void render(HttpResponse httpResponse) throws IOException {
        render(httpResponse, null);
    }

    @Override
    public void render(HttpResponse httpResponse, Object context) throws IOException {
        byte[] body = FileIoUtils.loadFileFromClasspath(TEMPLATE_PATH_PREFIX + url);
        httpResponse.addHeader("Content-Type", "text/html;charset=utf-8");
        httpResponse.changeBody(body);
    }
}
