package model.dto;

import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;

public class ResponseHeaders {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHeaders.class);

    private ResponseHeaders(){}

    public static void response200Header(DataOutputStream dos, String contentType, String cookie, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            dos.writeBytes("Content-Type: "+ contentType +";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response302Header(DataOutputStream dos, String cookie, String redirectUrl) {
        try {
            System.out.println(redirectUrl);
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            dos.writeBytes(String.format("Location: %s", redirectUrl));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
