package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static constant.DefaultConstant.DEFAULT_BODY;
import static constant.DefaultConstant.DEFAULT_PATH;
import static constant.PathConstant.STATIC;
import static constant.PathConstant.TEMPLATES;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             DataOutputStream dos = new DataOutputStream(out);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

            HttpRequest httpRequest = new HttpRequest(bufferedReader);

            byte[] body = DEFAULT_BODY;

            if (httpRequest.getMethod().equals("POST") && httpRequest.getUrl().equals("/user/create")) {
                DataBase.addUser(new User(
                        httpRequest.getParameter("userId"),
                        httpRequest.getParameter("password"),
                        httpRequest.getParameter("name"),
                        httpRequest.getParameter("email")
                ));

                response302Header(dos, "/index.html");
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
            return FileIoUtils.loadFileFromClasspath(STATIC + httpRequest.getUrl());
        }
        return FileIoUtils.loadFileFromClasspath(TEMPLATES + httpRequest.getUrl());
    }

    private boolean isStaticPath(String requestURL) {
        return getStaticFolderNames().contains(requestURL.split("/")[1]);
    }

    private Set<String> getStaticFolderNames() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(STATIC);
        File[] files = new File(resource.getPath()).listFiles();

        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    private void response200Header(DataOutputStream dos, HttpRequest httpRequest, int lengthOfBodyContent) {
        try {
            String contentType = Optional.ofNullable(httpRequest.getHeader("Accept"))
                    .map(str -> str.split(",")[0])
                    .orElse("text/html;charset=utf-8");

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + " \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + " \r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
