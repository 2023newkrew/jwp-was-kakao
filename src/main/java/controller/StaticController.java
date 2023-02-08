package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import model.dto.MyHeaders;
import model.dto.MyParams;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static model.dto.ResponseBodies.responseBody;
import static model.dto.ResponseHeaders.response200Header;

public class StaticController implements MyController{

    private final Logger logger = LoggerFactory.getLogger(StaticController.class);

    @Override
    public boolean canHandle(MyHeaders headers, MyParams params) {
        return isStatic(params.get("extension"));
    }

    @Override
    public void handle(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream) {
        String path = headers.get("path");

        handleStatic(path, dataOutputStream);
    }

    private void handleStatic(String path, DataOutputStream dataOutputStream){
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("static" + path);
            response200Header(dataOutputStream, body.length);
            responseBody(dataOutputStream, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isStatic(String extension){
        if(extension.equals("html") || extension.equals("ico")){
            return false;
        }
        return true;
    }
}
