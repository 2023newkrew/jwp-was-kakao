package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(final Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            // 입력값을 HttpRequest로 변환
            String uri = Parser.getURI(Parser.parseRequestMessage(reader));

            byte[] body = "".getBytes();
            DataOutputStream dos = new DataOutputStream(out);
            if (uri.endsWith(".html") | uri.endsWith(".css")) {
                body = Parser.getTargetBody(uri);
                response200Header(dos, body.length);
            }
            else {
                body = "Hello world".getBytes();
                response200Header(dos, body.length);
            }

            responseBody(dos, body);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
