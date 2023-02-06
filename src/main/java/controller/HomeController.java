package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static utils.ResponseBodies.responseBody;
import static utils.ResponseHeaders.*;

public class HomeController implements MyController{
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Override
    public boolean canHandle(MyHeaders headers, MyParams params) {
        String path = headers.get("path");
        return path.equals("/") || path.equals("/index.html") || path.equals("/favicon.ico");
    }

    @Override
    public void handle(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream) {
        String path = headers.get("path");
        String contentType = headers.get("contentType");

        if(path.equals("/favicon.ico") && headers.get("method").equals("GET")){
            getIco(path, contentType, dataOutputStream);
            return;
        }

        if(path.equals("/")){
            helloWorld(path, dataOutputStream);
            return;
        }

        if(path.equals("/index.html")){
            index(headers, params, dataOutputStream);
        }
    }

    private void getIco(String path, String contentType, DataOutputStream dataOutputStream){
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("templates" + path);
            response200Header(dataOutputStream, contentType, body.length);
            responseBody(dataOutputStream, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private void helloWorld(String path,  DataOutputStream dataOutputStream){;
        byte[] body = "Hello world".getBytes();
        response200Header(dataOutputStream, body.length);
        responseBody(dataOutputStream, body);
    }

    private void index(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream){
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("templates" + headers.get("path"));
            response200Header(dataOutputStream, headers.get("contentType"), body.length);
            responseBody(dataOutputStream, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
