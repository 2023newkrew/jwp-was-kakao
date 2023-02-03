package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = FileIoUtils.loadFileFromClasspath("./templates/index.html");
            String s = br.readLine();
            String[] tokens = s.split(" ");

            String requestMethod = null;
            String requestUrl  = null;
            String httpVersion = null;
            if (tokens.length == 3) {
                requestMethod = tokens[0]; // GET, POST 등
                requestUrl = tokens[1]; // /index.html
                httpVersion = tokens[2]; // HTTP/1.1
            }

            if (requestMethod.equals("GET")){
                if (requestUrl.equals("/index.html")){
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                }
                else{
                    body = "Hello world".getBytes();
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                }
            }

            while (s == null || "".equals(s)) {
                System.out.println(s);
                s = br.readLine();
            }


        } catch (IOException | URISyntaxException e) {
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
