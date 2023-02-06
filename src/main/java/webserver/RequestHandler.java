package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            HttpParser httpParser = new HttpParser(IOUtils.readHeader(br));
            String path = httpParser.getPath();
            byte[] body = new byte[0];

            switch (httpParser.getHttpMethod()){
                case "POST":
                    Integer contentLength = httpParser.getContentLength();
                    HashMap<String, String> requestBody = parseBody(IOUtils.readData(br, contentLength));

                    if(path.startsWith("/user/create")){
                        User user = new User(requestBody.get("userId"), requestBody.get("password"), requestBody.get("name"), requestBody.get("email"));
                        DataBase.addUser(user);
                        ResponseUtils.response302Header(dos, "/index.html");
                    }else{
                        ResponseUtils.response404Header(dos);
                    }
                    break;

                case "GET":
                    if(path.equals("/")){
                        ResponseUtils.response302Header(dos, "/index.html");
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

                case "PUT":
                case "DELETE":
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

    private HashMap<String, String> parseBody(String userBody){
        HashMap<String, String> result = new HashMap<>();

        for(String info : userBody.split("\\?")){
            String key = info.split("=")[0];
            String value = info.split("=")[1];
            result.put(key, value);
        }
        return result;
    }
}
