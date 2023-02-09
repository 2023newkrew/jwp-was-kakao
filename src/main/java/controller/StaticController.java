package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import model.dto.MyHeaders;
import model.dto.MyParams;
import webserver.response.ResponseEntity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static webserver.response.ResponseBodies.responseBody;
import static webserver.response.ResponseHeaders.response200Header;

public class StaticController implements MyController{

    private final Logger logger = LoggerFactory.getLogger(StaticController.class);
    private byte[] body;

    @Override
    public boolean canHandle(MyHeaders headers, MyParams params) {
        return isStatic(params.get("extension"));
    }

    @Override
    public ResponseEntity handle(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream) {
        String path = headers.get("path");
        String cookie = headers.get("cookie");
        String contentType = headers.get("contentType");

        return handleStatic(path, 200, contentType, cookie);
    }

    private ResponseEntity handleStatic(String path, int status, String contentType, String cookie){

        try {
            body = FileIoUtils.loadFileFromClasspath("static" + path);
        } catch (Exception e){
            logger.error(e.getMessage());
        }

        return ResponseEntity.builder()
                .status(200)
                .contentType(contentType)
                .cookie(cookie)
                .body(body)
                .build();
    }

    private boolean isStatic(String extension){
        if(extension.equals("html") || extension.equals("ico")){
            return false;
        }
        return true;
    }
}
