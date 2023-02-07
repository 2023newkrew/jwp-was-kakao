package app.controller;

import infra.Controller;
import utils.FileIoUtils;
import infra.http.Headers;
import infra.http.request.HttpRequest;
import infra.http.response.HttpResponse;
import infra.http.response.HttpResponseStatus;

import java.io.IOException;
import java.net.URISyntaxException;

public class ViewController implements Controller {
    public static String URI_ROOT = "/index.html";
    public static String URI_CSS = "./css";
    private static String PATH_TEMPLATES = "./templates";
    private static String PATH_STATIC = "./static";
    private static String TYPE_HTML = "text/html;charset=utf-8";
    private static String TYPE_CSS = "text/css;charset=utf-8";

    public HttpResponse response(HttpRequest request) {
        if (!request.isGET()) {
            return new HttpResponse(HttpResponseStatus.BAD_REQUEST);
        }

        try {
            String uri = request.getUri();
            if (uri.equals(URI_ROOT)) {
                return this.getRoot();
            }
            if (uri.startsWith(URI_CSS)) {
                return this.getCss(uri.substring(1));
            }
            return new HttpResponse(HttpResponseStatus.NOT_FOUND);
        } catch (URISyntaxException e) {
            return new HttpResponse(HttpResponseStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpResponse getRoot() throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath(PATH_TEMPLATES + URI_ROOT);
        HttpResponse response = new HttpResponse(HttpResponseStatus.OK, body);
        response.setHeader(Headers.CONTENT_TYPE, TYPE_HTML);
        response.setHeader(Headers.CONTENT_LENGTH, String.valueOf(body.length));
        return response;
    }

    private HttpResponse getCss(String path) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath(PATH_STATIC + path);
        HttpResponse response = new HttpResponse(HttpResponseStatus.OK, body);
        response.setHeader(Headers.CONTENT_TYPE, TYPE_CSS);
        response.setHeader(Headers.CONTENT_LENGTH, String.valueOf(body.length));
        return response;
    }
}
