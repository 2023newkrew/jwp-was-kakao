package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import webserver.http.*;
import webserver.http.session.Session;
import webserver.http.session.SessionManager;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final String CRLF = "\r\n";

    private final Socket connection;

    public RequestHandler(final Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            HttpResponse response = new HttpResponse();
            HttpRequest request = RequestParser.parseRequestMessage(reader, response);

            HttpCookie sessionCookie = request.getCookie("JSESSIONID");
            String jsessionId;
            /* sessionid 생성 */
            if (Objects.isNull(sessionCookie)) {
                UUID uuid = UUID.randomUUID();
                response.setHeader("Set-Cookie", "JSESSIONID=" + uuid + "; Path=/");
                jsessionId = uuid.toString();
                SessionManager.add(new Session(jsessionId));
            } else {
                jsessionId = sessionCookie.getValue();
            }

            /* 이미 로그인 되어 있으면 리다이렉트 */
            if (request.getPath().equals("/user/login.html")) {
                User user = (User) SessionManager.findSession(jsessionId).getAttribute("user");
                if (Objects.nonNull(user)) {
                    response.setHeader("Location", "/index.html");
                    response.setStatus(HttpStatus.FOUND);
                }
            }

            if (Objects.nonNull(response.getStatus())) {
                sendResponse(response, dos);
                return;
            }

            String path = request.getPath();

            HandlerMapping handlerMapping = new HandlerMapping();
            Handler handler = handlerMapping.getHandler(path);

            if (Objects.isNull(handler)) { /* 매치되는 Handler가 없을 경우 */
                if (path.equals("/")) {
                    response.setBody("Hello world".getBytes());
                } else { /* file 요청 처리 */
                    response.setBody(RequestParser.getFileContent(request.getPath(), response));
                }
                if (Objects.isNull(response.getStatus())) {
                    response.setStatus(HttpStatus.OK);
                }
            } else { /* 매치되는 Handler가 있을 경우 */
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
                response302Header(dos);
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
        responseCommonHeader(dos, response);
        responseBody(dos, response.getBody());
    }

    private void responseCommonHeader(final DataOutputStream dos, final HttpResponse response) {
        Map<String, HttpHeader> headers = response.getHeaders();
        headers.keySet().forEach(headerName -> {
            try {
                dos.writeBytes(headerName + ": " + headers.get(headerName).getValue() + " " + CRLF);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        });
        try {
            dos.writeBytes(CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK " + CRLF);
            dos.writeBytes("Content-Type: text/html;charset=utf-8 " + CRLF);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " " + CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found " + CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response400Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request " + CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found " + CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response405Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 405 Method Not Allowed " + CRLF);
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
