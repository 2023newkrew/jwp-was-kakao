package controller;

import type.ContentType;
import type.HttpStatusCode;
import webserver.HttpRequest;
import webserver.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;

public class HelloController extends Controller {

    @Override
    public void doGet(HttpRequest request, DataOutputStream dos){
        String data = "Hello world";
        Integer contentLength = data.length();

        try {
            dos.writeBytes(ResponseHeader.of(HttpStatusCode.OK, ContentType.HTML, contentLength).getValue());
            byte[] body = data.getBytes();
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void doPost(HttpRequest request, DataOutputStream dos) {

    }

    @Override
    public void doFinally(HttpRequest request, DataOutputStream dos) {

    }
}
