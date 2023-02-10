package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import model.dto.MyHeaders;
import model.dto.MyParams;
import webserver.response.ResponseEntity;

import java.io.DataOutputStream;

public class StaticController implements MyController{

    private final Logger logger = LoggerFactory.getLogger(StaticController.class);
    private byte[] body;

    @Override
    public boolean canHandle(MyHeaders headers, MyParams params) {
        String extension = getExtensionFromPath(params.get("path"));
        return isStatic(extension);
    }

    @Override
    public ResponseEntity handle(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream) {
        String path = headers.get("path");
        String contentType = headers.get("contentType");

        return handleStatic(path, 200, contentType);
    }

    private ResponseEntity handleStatic(String path, int status, String contentType){

        try {
            body = FileIoUtils.loadFileFromClasspath("static" + path);
        } catch (Exception e){
            logger.error(e.getMessage());
        }

        return ResponseEntity.builder()
                .status(status)
                .contentType(contentType)
                .body(body)
                .build();
    }

    private boolean isStatic(String extension){
        if(extension.equals("html") || extension.equals("ico")){
            return false;
        }
        return true;
    }

    private String getExtensionFromPath(String path){
        String[] tokens = path.split("\\.");
        if(tokens.length == 0) return "";
        return tokens[tokens.length - 1];
    }
}
