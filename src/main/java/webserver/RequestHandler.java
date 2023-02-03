package webserver;

import constant.HttpMethod;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.ResponseUtils;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Handler;
import java.util.stream.Collectors;

import static constant.DefaultConstant.*;
import static constant.PathConstant.*;
import static utils.FileIoUtils.*;
import static utils.RequestBuilder.*;
import static utils.ResponseUtils.*;
import static webserver.FrontController.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             DataOutputStream dos = new DataOutputStream(out);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

            HttpRequest httpRequest = getHttpRequest(bufferedReader);

            byte[] body = DEFAULT_BODY;

            if (httpRequest.isHttpMethodEquals(HttpMethod.POST) && httpRequest.isURLEquals("/user/create")) {
                FrontController.handleRequest(httpRequest, dos);

//                Map<String, String> requestBody = httpRequest.getBody();
//                DataBase.addUser(new User(
//                        requestBody.get("userId"),
//                        requestBody.get("password"),
//                        requestBody.get("name"),
//                        requestBody.get("email"))
//                );
//
//                response302Header(dos, "/index.html");
                return;
            }

            if (!httpRequest.getUrl().equals(DEFAULT_PATH)) {
                String requestURL = httpRequest.getUrl();
                body = setBody(httpRequest, requestURL);
            }

            response200Header(dos, httpRequest, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] setBody(HttpRequest httpRequest, String requestURL) throws IOException, URISyntaxException {
        if (isStaticPath(requestURL)) {
            return loadFileFromClasspath(STATIC + httpRequest.getUrl());
        }
        return loadFileFromClasspath(TEMPLATES + httpRequest.getUrl());
    }

    private boolean isStaticPath(String requestURL) {
        return getStaticFolderNames().contains(requestURL.split("/")[1]);
    }

}
