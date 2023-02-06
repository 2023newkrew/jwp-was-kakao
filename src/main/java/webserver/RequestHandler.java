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
import java.util.List;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String PROTOCOL = "HTTP";
    private static final String VERSION = "1.1";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()
        ) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            RequestParser requestParser = new RequestParser(bufferedReader);
            RequestHeader header = requestParser.getRequestHeader();
            //TODO: RequestHeader에 URI가 포함되지 않은 상황을 고려해야 할까?
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

            List<String> roots = List.of("templates", "static");
            byte[] returnBody = null;

            for (String root : roots) {
                returnBody = FileIoUtils.loadFileFromClasspath(root + uri);
                if (returnBody != null) {
                    String[] split = uri.split("\\.");
                    String fileType = split[split.length - 1];
                    dos.writeBytes(
                            ResponseHeader.of(HttpStatusCode.OK,
                                            ContentType.valueOf(fileType.toUpperCase()),
                                            returnBody.length)
                                    .getValue()
                    );
                    break;
                }
            }
            if (returnBody == null) {
                dos.writeBytes(ResponseHeader.of(HttpStatusCode.NOT_FOUND, ContentType.HTML).getValue());
            }
            responseBody(dos, returnBody);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            return;
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
