package controller;

import type.ContentType;
import type.HttpStatusCode;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ResponseHeader;

public class HelloController extends Controller {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        String data = "Hello world";
        Integer contentLength = data.length();

        ResponseHeader header = response.getResponseHeader();
        header.setHttpStatusCode(HttpStatusCode.OK);
        header.setContentType(ContentType.HTML);
        header.setContentLength(contentLength);

        response.setResponseHeader(header);
        response.setResponseBody(data.getBytes());
    }
}
