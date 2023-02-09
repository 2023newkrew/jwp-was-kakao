package webserver;

import Controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.HttpCookie;
import session.SessionManager;
import utils.FileIoUtils;
import utils.HttpParser;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String TEMPLATE_ROOT_PATH = "./templates";
    private static final String STATIC_ROOT_PATH = "./static";
    private static final String SESSION_KEY = "JSESSIONID";

    private final Socket connection;
    private final UserController userController;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.userController = UserController.getInstance();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            HttpParser httpParser = new HttpParser(IOUtils.readHeader(br));
            String path = httpParser.getPath();
            HttpCookie httpCookie = httpParser.getCookie();
            byte[] body = new byte[0];

            switch (httpParser.getHttpMethod()){
                case POST:
                    Integer contentLength = httpParser.getContentLength();
                    HashMap<String, String> requestBody = parseQueryString(IOUtils.readData(br, contentLength));

                    if(path.equals("/user/create")){
                        userController.createUser(dos, requestBody);
                    }else if(path.equals("/user/login")){
                        userController.login(dos, requestBody);
                    }else{
                        ResponseUtils.response404Header(dos);
                    }
                    break;

                case GET:
                    HashMap<String, String> queryParam = null;
                    if(path.contains("?")) queryParam = parseQueryString(path.substring(path.indexOf('?') + 1));

                    if(path.equals("/")){
                        ResponseUtils.response302Header(dos, "/index.html");
                    }else if(path.equals("/user/list.html")){
                        body = userController.getUserList(dos, path, httpCookie);
                    }else if(path.equals("/user/login.html")){
                        String sessionId = httpCookie.getCookieValueByKey(SESSION_KEY);
                        if(SessionManager.findSession(sessionId) != null){
                            ResponseUtils.response302Header(dos, "/index.html");
                        }else{
                            body = FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + path);
                            ResponseUtils.response200Header(dos, body.length, path);
                        }
                    }else if(path.endsWith(".html") || path.endsWith("/favicon.ico")){
                        body = FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + path);
                        ResponseUtils.response200Header(dos, body.length, path);
                    }else if(path.startsWith("/css") || path.startsWith("/fonts") || path.startsWith("/images") || path.startsWith("/js")){
                        body = FileIoUtils.loadFileFromClasspath(STATIC_ROOT_PATH + path);
                        ResponseUtils.response200Header(dos, body.length, path);
                    }else{
                        ResponseUtils.response404Header(dos);
                    }
                    break;

                case PUT:
                case DELETE:
                    ResponseUtils.response404Header(dos);
                    break;

                default:
                    ResponseUtils.response400Header(dos);
            }

            ResponseUtils.responseBody(dos, body);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private HashMap<String, String> parseQueryString(String requestBody){
        HashMap<String, String> result = new HashMap<>();

        for(String info : requestBody.split("&")){
            String key = info.split("=")[0];
            String value = info.split("=")[1];
            result.put(key, value);
        }
        return result;
    }
}
