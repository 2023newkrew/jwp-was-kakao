package utils;

import java.io.IOException;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class HttpResponseUtils {
    private static final String HTML_CONTENT_TYPE = "text/html;charset=utf-8";
    public static HttpResponse responseTemplatePage(HttpRequest httpRequest, String path, boolean existError) {
        try {
            String registerFailedPage = RenderUtils.renderErrorPageData(path, existError);
            return HttpResponse.ok(httpRequest, registerFailedPage.getBytes(), HTML_CONTENT_TYPE);
        } catch (IOException e) {
            return HttpResponse.pageNotFound();
        }
    }
}
