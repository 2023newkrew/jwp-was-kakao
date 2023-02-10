package controller;

import webserver.request.HttpRequest;
import webserver.request.MyHeaders;
import webserver.request.MyParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;
import webserver.response.ResponseEntity;

import java.io.DataOutputStream;

public class HomeController implements MyController{
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private byte[] body;
    @Override
    public boolean canHandle(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        return parsePath(path);
    }

    @Override
    public ResponseEntity handle(HttpRequest httpRequest, DataOutputStream dataOutputStream) {
        String path = httpRequest.getPath();
        String cookie = httpRequest.getCookie().toString();
        String contentType = httpRequest.getContentLength();

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

    private boolean parsePath(String path){
        if(path.equals("/") || path.equals("/index.html") || path.equals("/favicon.ico")){
            return true;
        }
        return false;
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
