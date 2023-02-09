package controller;

import type.ContentType;
import type.HttpStatusCode;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;

public class HelloController extends Controller {

    @Override
    public void doGet(HttpRequest request, HttpResponse response, DataOutputStream dos){
        String data = "Hello world";
        Integer contentLength = data.length();

        response.setResponseHeader(ResponseHeader.of(HttpStatusCode.OK, ContentType.HTML, contentLength));
        response.setResponseBody(data.getBytes());
    }
}
