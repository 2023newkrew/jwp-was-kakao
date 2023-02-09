package webserver.utils;

import webserver.response.HttpResponse;
import webserver.response.HttpResponseContentType;

import static webserver.response.HttpResponseHeader.*;

public class ResponseUtil {

    public static void response302(HttpResponse response, String redirectUrl) {
        response.addHeader(LOCATION, redirectUrl);
    }

    public static void response200(HttpResponse response, byte[] body, HttpResponseContentType contentType) {
        response.addHeader(CONTENT_TYPE, contentType.getContentType());
        response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
        response.addBody(body);
    }
}
