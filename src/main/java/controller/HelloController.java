package controller;

import type.ContentType;
import type.HttpStatusCode;
import webserver.HttpRequest;
import webserver.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;

public class HelloController extends Controller {
    @Override
    public void process(HttpRequest request, DataOutputStream dos) throws IOException {
        String reqMethod = request.getRequestHeader().get("method").orElseThrow(IllegalArgumentException::new);

        if (reqMethod.equals("GET")) {
            String data = "Hello world";
            Integer contentLength = data.length();

            dos.writeBytes(ResponseHeader.of(HttpStatusCode.OK, ContentType.HTML, contentLength).getValue());
            byte[] body = data.getBytes();

            responseBody(dos, body);
        }

    }
}
