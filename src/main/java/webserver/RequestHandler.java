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
            byte[] body;
            String s = br.readLine();
            String[] tokens = s.split(" "); // GET URL HTTP/1.1

            while (!"".equals(s)) {
                s = br.readLine();
            }
            if (s == null) {
                return;
            }

            String requestMethod = null;
            String requestUrl  = null;
            String httpVersion = null;
            if (tokens.length == 3) {
                requestMethod = tokens[0]; // GET, POST 등
                requestUrl = tokens[1]; // /index.html
                httpVersion = tokens[2]; // HTTP/1.1

            }
            logger.debug("request method : {}, requestUrl : {}, httpVersion : {}", requestMethod, requestUrl, httpVersion);
            if (requestMethod.equals("GET")){
                if (requestUrl.startsWith("/css") || requestUrl.startsWith("/js")){
                    System.out.println("static : " + requestUrl);
                    requestUrl = "./static" + requestUrl;
                    body = FileIoUtils.loadFileFromClasspath(requestUrl);
                    String[] urlSplitByDot = requestUrl.split("\\.");
                    response200Header(dos, body.length, urlSplitByDot[urlSplitByDot.length-1]);
                    responseBody(dos, body);
                }
                else if (requestUrl.startsWith("/") && requestUrl.contains(".")){
                    System.out.println("templates : " + requestUrl);
                    requestUrl = "./templates" + requestUrl;
                    body = FileIoUtils.loadFileFromClasspath(requestUrl);
                    String[] urlSplitByDot = requestUrl.split("\\.");
                    response200Header(dos, body.length, urlSplitByDot[urlSplitByDot.length-1]);
                    responseBody(dos, body);
                    try {
                        Thread.sleep(100);
                    } catch(Exception e){

                    }
                }
                else{
                    body = "Hello world".getBytes();
                    response200Header(dos, body.length, "html");
                    responseBody(dos, body);
                }
            }



        } catch (IOException | URISyntaxException | RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + type + ";charset=utf-8 \r\n");
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
