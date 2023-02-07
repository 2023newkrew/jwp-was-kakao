package controller;

import http.HttpHeaders;
import http.HttpRequest;
import http.HttpResponse;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class ResourceController {

    private static ResourceController INSTANCE;
    private ResourceController() { }

    public static synchronized ResourceController getInstance() {
        if(INSTANCE == null)
            INSTANCE = new ResourceController();
        return INSTANCE;
    }

    public HttpResponse staticResource(HttpRequest request) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath("static" + request.getPath());
        HttpResponse response = new HttpResponse.Builder()
                .addAttribute(HttpHeaders.CONTENT_TYPE, "text/css;" + "charset=utf-8")
                .body(body)
                .build();

        return response;
    }

    public HttpResponse templateResource(HttpRequest request) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath("templates" + request.getPath());
        HttpResponse response = new HttpResponse.Builder()
                .addAttribute(HttpHeaders.CONTENT_TYPE, "text/html;" + "charset=utf-8")
                .body(body)
                .build();
        return response;
    }
}
