package controller;

import model.dto.MyHeaders;
import model.dto.MyParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static model.dto.ResponseBodies.responseBody;
import static model.dto.ResponseHeaders.*;

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
        String cookie = headers.get("cookie");
        String contentType = headers.get("contentType");

        if(path.equals("/favicon.ico") && headers.get("method").equals("GET")){
            getIco(path, contentType, cookie, dataOutputStream);
            return;
        }

        if(path.equals("/")){
            helloWorld(contentType, cookie, dataOutputStream);
            return;
        }

        if(path.equals("/index.html")){
            index(path, contentType, cookie, dataOutputStream);
        }
    }

    private void getIco(String path, String contentType, String cookie, DataOutputStream dataOutputStream){
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("templates" + path);
            response200Header(dataOutputStream, contentType, cookie, body.length);
            responseBody(dataOutputStream, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private void helloWorld(String contentType, String cookie, DataOutputStream dataOutputStream){;
        byte[] body = "Hello world".getBytes();
        response200Header(dataOutputStream, contentType, cookie, body.length);
        responseBody(dataOutputStream, body);
    }

    private void index(String path, String contentType, String cookie, DataOutputStream dataOutputStream){
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("templates" + path);
            response200Header(dataOutputStream, contentType, cookie, body.length);
            responseBody(dataOutputStream, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
