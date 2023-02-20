package webserver.utils;

import webserver.response.HttpResponse;
import webserver.response.HttpResponseContentType;
import webserver.response.HttpResponseStatus;

import static webserver.response.HttpResponseHeader.*;

public class ResponseUtil {

    public static void response302(HttpResponse response, String redirectUrl) {
        response.setStatusLine(HttpResponseStatus.REDIRECT);
        response.addHeader(LOCATION, redirectUrl);
    }

    public static void response200(HttpResponse response, byte[] body, HttpResponseContentType contentType) {
        response.setStatusLine(HttpResponseStatus.OK);
        response.addHeader(CONTENT_TYPE, contentType.getContentType());
        response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
        response.addBody(body);
    }

    public static void response400(HttpResponse response, String message) {
        response.setStatusLine(HttpResponseStatus.BAD_REQUEST);
        response.addHeader(CONTENT_TYPE, HttpResponseContentType.PLAIN.getContentType());
        response.addHeader(CONTENT_LENGTH, String.valueOf(message.length()));
        response.addBody(message.getBytes());
    }
}
