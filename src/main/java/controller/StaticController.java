package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.request.HttpRequest;
import webserver.request.MyHeaders;
import webserver.request.MyParams;
import webserver.response.ResponseEntity;

import java.io.DataOutputStream;

public class StaticController implements MyController{

    private final Logger logger = LoggerFactory.getLogger(StaticController.class);

    private byte[] body;

    @Override
    public boolean canHandle(HttpRequest httpRequest) {
        String extension = getExtensionFromPath(httpRequest.getPath());
        return isStatic(extension);
    }

    @Override
    public ResponseEntity handle(HttpRequest httpRequest, DataOutputStream dataOutputStream) {
        String path = httpRequest.getPath();
        String contentType = httpRequest.getContentType();

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
