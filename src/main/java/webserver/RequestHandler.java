package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.ContentType;
import type.HttpStatusCode;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logConnected();

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()
        ) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            RequestParser requestParser = new RequestParser(bufferedReader);
            RequestHeader header = requestParser.getRequestHeader();

            String uri = header.get("URI").orElseThrow(IllegalArgumentException::new);

            DataOutputStream dos = new DataOutputStream(out);

            // 유저 등록 상황
            if (uri.startsWith("/user/create")) {
                User user = new User(
                        requestParser.getParam("userId"),
                        requestParser.getParam("password"),
                        requestParser.getParam("name"),
                        requestParser.getParam("email")
                );
                DataBase.addUser(user);
                // 생성 후 index로 redirect
                dos.writeBytes(ResponseHeader.of(HttpStatusCode.REDIRECT, "/index.html").getValue());
                responseBody(dos, new byte[0]);
                return;
            }

            byte[] returnBody = null;

            String root = "static";
            String[] split = uri.split("\\.");
            String fileType = split[split.length - 1];

            if (fileType.equals("html")) {
                root = "templates";
            }

            returnBody = FileIoUtils.loadFileFromClasspath(root + uri);

            if (returnBody == null) {
                dos.writeBytes(ResponseHeader.of(HttpStatusCode.NOT_FOUND, ContentType.HTML).getValue());
                dos.flush();
                return;
            }

            dos.writeBytes(
                    ResponseHeader.of(HttpStatusCode.OK,
                                    ContentType.valueOf(fileType.toUpperCase()),
                                    returnBody.length)
                            .getValue()
            );
            responseBody(dos, returnBody);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private void logConnected() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
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
