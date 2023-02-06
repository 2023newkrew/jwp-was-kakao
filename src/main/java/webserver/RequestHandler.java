package webserver;

import http.Headers;
import http.HttpMethod;
import common.Protocol;
import http.HttpUrl;
import http.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            RequestInfo requestInfo = parseRequestInfo(br.readLine());
            Headers headers = new Headers();

            while(true){
                String line = br.readLine();
                if(Objects.isNull(line) || !"".equals(line)) {
                    break;
                }

                line = line.substring(0, line.indexOf("/r/n"));
                headers.put(line);
            }

            // TODO
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "Hello world".getBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // GET /index.html HTTP/1.1
    private RequestInfo parseRequestInfo(String rawRequestInfo) {
        String[] requestInfo = rawRequestInfo.split(" ");
        HttpMethod httpMethod = HttpMethod.valueOf(requestInfo[0]);
        HttpUrl url = new HttpUrl(requestInfo[1]);
        Protocol protocol = Protocol.from(requestInfo[2]);

        return new RequestInfo(httpMethod, url, protocol);
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
