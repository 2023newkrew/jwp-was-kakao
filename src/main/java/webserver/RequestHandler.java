package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    private final Map<String, String> headers = new HashMap<>();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();

            if (line == null || "".equals(line)) {
                throw new RuntimeException("잘못된 요청 형식입니다.");
            }

            String[] tokens = line.split(" ");
            String url = tokens[1];

            line = br.readLine();

            while (!"".equals(line)) {
                if (line == null) { return; }
                System.out.println(line);
                String[] header = line.split(": ");
                headers.put(header[0], header[1]);
                line = br.readLine();
            }

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body;
            if (url.equals("/")) {
                body = "Hello world".getBytes();
            } else if (url.equals("/index.html")) {
                body = FileIoUtils.loadFileFromClasspath("./templates" + url);
            } else {
                body = FileIoUtils.loadFileFromClasspath("./static" + url);
            }

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            String contentType = headers.getOrDefault("Accept", "text/html").split(",")[0];
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8 \r\n");
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
