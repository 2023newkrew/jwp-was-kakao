package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(final Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            HttpResponse response = new HttpResponse();
            HttpRequest request = RequestParser.parseRequestMessage(reader, response);

            if (Objects.nonNull(response.getStatus())) {
                sendResponse(response, dos);
                return;
            }

            String path = request.getPath();

            HandlerMapping handlerMapping = new HandlerMapping();
            Handler handler = handlerMapping.getHandler(path);
            /* 매치되는 Handler가 없을 경우 */
            if (Objects.isNull(handler)) {
                if (path.equals("/")) {
                    response.setBody("Hello world".getBytes());
                }
                /* file 요청 처리 */
                else {
                    response.setBody(RequestParser.getFileContent(request.getPath(), response));
                }
                if (Objects.isNull(response.getStatus())) {
                    response.setStatus(HttpStatus.OK);
                }
            }
            /* 매치되는 Handler가 있을 경우 */
            else {
                handler.service(request, response);
            }
            sendResponse(response, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendResponse(HttpResponse response, DataOutputStream dos) {
        switch (response.getStatus()) {
            case OK:
                response200Header(dos, response.getBody().length);
                break;
            case FOUND:
                response302Header(dos, response.getHeader("Location"));
                break;
            case BAD_REQUEST:
                response400Header(dos);
                break;
            case NOT_FOUND:
                response404Header(dos);
                break;
            case METHOD_NOT_ALLOWED:
                response405Header(dos);
                break;
        }
        responseBody(dos, response.getBody());
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: "+location+" \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response400Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response405Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 405 Method Not Allowed \r\n");
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
