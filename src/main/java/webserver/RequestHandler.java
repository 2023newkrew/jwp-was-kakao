package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.ContentType;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            List<String> roots = List.of("templates", "static");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            RequestParser requestParser = new RequestParser(bufferedReader);

            RequestHeader header = requestParser.getRequestHeader();
            Map<String, String> requestParameter = requestParser.getParams();

            DataOutputStream dos = new DataOutputStream(out);
            //TODO: RequestHeader에 URI가 포함되지 않은 상황을 고려해야 할까?
            byte[] returnBody = null;
            String uri = header.get("URI").orElseThrow(RuntimeException::new);

            String[] uriSplit = uri.split("\\?");
            String uriWithOutParams = uriSplit[0];

            if (uriWithOutParams.equals("/user/create")) {
                User user = User.from(requestParameter);
                DataBase.addUser(user);
                response302Header(dos);
                responseBody(dos, new byte[0]);
                return;
            }

            for (String root : roots) {
                returnBody = FileIoUtils.loadFileFromClasspath(root + uri);
                if (returnBody != null) {
                    String[] split = uri.split("\\.");
                    String ext = split[split.length - 1];
                    response200Header(dos, returnBody.length, ContentType.valueOf(ext.toUpperCase()));
                    break;
                }
            }
            if (returnBody == null) {
                response404Header(dos);
            }
            responseBody(dos, returnBody);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            return;
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, ContentType type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type.getToResponseText() + " \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response201Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 201 CREATED \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 REDIRECT \r\n");
            dos.writeBytes("Location: /index.html");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 NOT FOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("\r\n");
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
