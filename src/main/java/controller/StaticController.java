package controller;

import model.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.MyHeaders;
import utils.MyParams;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static utils.ResponseBodies.responseBody;
import static utils.ResponseHeaders.response200Header;

public class StaticController implements MyController{

    private final Logger logger = LoggerFactory.getLogger(StaticController.class);

    @Override
    public boolean canHandle(MyHeaders headers, MyParams params, Extension extension) {
        return extension.isStatic();
    }

    @Override
    public void handle(MyHeaders headers, MyParams params, Extension extension, DataOutputStream dataOutputStream) {
        String path = headers.get("path");
        String contentType = headers.get("contentType");

        handleStatic(path, contentType, dataOutputStream);
    }

    private void handleStatic(String path, String contentType, DataOutputStream dataOutputStream){
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("static" + path);
            response200Header(dataOutputStream, contentType, body.length);
            responseBody(dataOutputStream, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
