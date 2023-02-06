package webserver;

import http.HttpHeaders;
import http.HttpRequest;
import http.HttpResponse;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class ResourceController {
    public HttpResponse staticResource(HttpRequest request) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath("static" + request.getUri().getPath());
        HttpResponse response = new HttpResponse.Builder()
                .addAttribute(HttpHeaders.CONTENT_TYPE, "text/css;" + "charset=utf-8")
                .body(body)
                .build();

        return response;
    }

    public HttpResponse templateResource(HttpRequest request) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath("templates" + request.getUri().getPath());
        HttpResponse response = new HttpResponse.Builder()
                .addAttribute(HttpHeaders.CONTENT_TYPE, "text/html;" + "charset=utf-8")
                .body(body)
                .build();
        return response;
    }
}
