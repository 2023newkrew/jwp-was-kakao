package Controller;

import session.HttpCookie;
import session.SessionManager;
import utils.FileIoUtils;
import webserver.ResponseUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class ResourceController {
    private static final ResourceController INSTANCE = new ResourceController();
    private static final String TEMPLATE_ROOT_PATH = "./templates";
    private static final String STATIC_ROOT_PATH = "./static";
    private static final String SESSION_KEY = "JSESSIONID";

    private ResourceController(){}

    public static ResourceController getInstance(){
        return INSTANCE;
    }

    public byte[] getCommonTemplate(DataOutputStream dos, String path) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + path);
        ResponseUtils.response200Header(dos, body.length, path);

        return body;
    }

    public byte[] getStatic(DataOutputStream dos, String path) throws IOException, URISyntaxException{
        byte[] body = FileIoUtils.loadFileFromClasspath(STATIC_ROOT_PATH + path);
        ResponseUtils.response200Header(dos, body.length, path);

        return body;
    }

    public byte[] getLoginTemplate(DataOutputStream dos, String path, HttpCookie httpCookie) throws IOException, URISyntaxException{
        byte[] body = new byte[0];
        String sessionId = httpCookie.getCookieValueByKey(SESSION_KEY);
        if(SessionManager.findSession(sessionId) != null){
            ResponseUtils.response302Header(dos, "/index.html");
            return body;
        }
        body = FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + path);
        ResponseUtils.response200Header(dos, body.length, path);

        return body;
    }
}
