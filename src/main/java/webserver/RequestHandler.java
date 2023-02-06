package webserver;

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
    private static final List<String> roots = List.of("templates", "static");

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dataOutputStream = new DataOutputStream(out);

            Request request = new Request(bufferedReader);
            Map<String, String> requestParameter = request.getRequestParams();

            byte[] returnBody = null;
            String uri = request.getUri();

            if (HandlerMapper.INSTANCE.isHandleAvailable(uri)) {
                Response response = HandlerMapper.INSTANCE.handle(uri, requestParameter);
                writeResponse(dataOutputStream, response.toBytes());
                return;
            }

            for (String root : roots) {
                returnBody = FileIoUtils.loadFileFromClasspath(root + uri);
                if (returnBody != null) {
                    String[] split = uri.split("\\.");
                    String ext = split[split.length - 1];
                    response200Header(dataOutputStream, returnBody.length, ContentType.valueOf(ext.toUpperCase()));
                    break;
                }
            }
            if (returnBody == null) {
                response404Header(dataOutputStream);
            }
            responseBody(dataOutputStream, returnBody);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException ignored) {
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, ContentType type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type.getString() + " \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
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

    private void writeResponse(DataOutputStream dos, byte[] response) {
        try {
            dos.write(response);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
