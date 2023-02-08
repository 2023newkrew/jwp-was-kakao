package app.controller;

import app.controller.support.ResourceType;
import infra.Controller;
import infra.http.body.ByteBody;
import infra.http.header.ContentType;
import infra.http.header.Headers;
import infra.http.request.HttpRequest;
import infra.http.response.HttpResponse;
import infra.http.response.ResponseStatus;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class ViewController implements Controller {
    private static String PATH_TEMPLATES = "./templates";
    private static String PATH_STATIC = "./static";

    public HttpResponse response(HttpRequest request) {
        if (!request.isGET()) {
            return new HttpResponse(ResponseStatus.BAD_REQUEST);
        }
        try {
            String uri = request.getUri();
            if (uri.endsWith(ResourceType.HTML.value())) {
                return this.getHtml(uri);
            }
            if (uri.endsWith(ResourceType.CSS.value())) {
                return this.getCss(uri);
            }
            return new HttpResponse(ResponseStatus.NOT_FOUND);
        } catch (URISyntaxException e) {
            return new HttpResponse(ResponseStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new HttpResponse(ResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpResponse getHtml(String path) throws IOException, URISyntaxException {
        return this.getResource(PATH_TEMPLATES + path, ContentType.UTF8_HTML);
    }

    private HttpResponse getCss(String path) throws IOException, URISyntaxException {
        return this.getResource(PATH_STATIC + path, ContentType.UTF8_CSS);
    }

    private HttpResponse getResource(String path, ContentType contentType) throws IOException, URISyntaxException {
        ByteBody body = new ByteBody(FileIoUtils.loadFileFromClasspath(path));
        HttpResponse response = new HttpResponse(ResponseStatus.OK, body);
        response.setHeader(Headers.CONTENT_TYPE, contentType.value());
        response.setHeader(Headers.CONTENT_LENGTH, String.valueOf(body.length()));
        return response;
    }
}
