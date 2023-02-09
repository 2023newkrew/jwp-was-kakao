package controller;

import model.dto.MyHeaders;
import model.dto.MyParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;
import webserver.response.ResponseEntity;

import java.io.DataOutputStream;

public class HomeController implements MyController{
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private byte[] body;
    @Override
    public boolean canHandle(MyHeaders headers, MyParams params) {
        String path = headers.get("path");
        return path.equals("/") || path.equals("/index.html") || path.equals("/favicon.ico");
    }

    @Override
    public ResponseEntity handle(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream) {
        String path = headers.get("path");
        String cookie = headers.get("cookie");
        String contentType = headers.get("contentType");

        try{
            body = FileIoUtils.loadFileFromClasspath("templates" + path);
        } catch (Exception e){
            logger.error(e.getMessage());
        }

        if(path.equals("/")) {
            body = "Hello world".getBytes();
        }

        return getResponseEntity(200, contentType, cookie, body);
    }

    private ResponseEntity getResponseEntity(int status, String contentType, String cookie, byte[] body){
        return ResponseEntity.builder()
                .status(status)
                .contentType(contentType)
                .cookie(cookie)
                .body(body)
                .build();
    }
}
