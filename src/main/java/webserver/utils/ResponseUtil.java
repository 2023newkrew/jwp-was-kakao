package webserver.utils;

import webserver.response.HttpResponse;
import webserver.response.HttpResponseContentType;

import static webserver.response.HttpResponseHeader.*;
import static webserver.response.HttpResponseStatus.OK;
import static webserver.response.HttpResponseStatus.REDIRECT;

public class ResponseUtil {

    public static void response302(HttpResponse response, String redirectUrl) {
        response.setStatusLine(REDIRECT);
        response.addHeader(LOCATION, redirectUrl);
    }

    public static void response200(HttpResponse response) {
        response.setStatusLine(OK);
    }

    public static void response200(HttpResponse response, byte[] body, HttpResponseContentType contentType) {
        response.setStatusLine(OK);
        response.addHeader(CONTENT_TYPE, contentType.getContentType());
        response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
        response.addBody(body);
    }
}
