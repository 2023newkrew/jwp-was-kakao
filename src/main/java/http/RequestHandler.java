package http;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Objects;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final RequestParser requestParser;

    public RequestHandler(Socket connectionSocket, RequestParser requestParser) {
        this.connection = connectionSocket;
        this.requestParser = requestParser;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = requestParser.getBufferReader(in);
            DataOutputStream dos = requestParser.getOutputStream(out);
            byte[] body;

            String s = br.readLine();
            String[] tokens = requestParser.splitSpare(s);

            String requestMethod = null;
            String requestUrl  = null;
            String httpVersion = null;
            if (tokens.length == 3) {
                requestMethod = tokens[0]; // GET, POST 등
                requestUrl = tokens[1]; // /index.html
                httpVersion = tokens[2]; // HTTP/1.1
            }
            logger.debug("request method : {}, requestUrl : {}, httpVersion : {}", requestMethod, requestUrl, httpVersion);
            if (Objects.requireNonNull(requestMethod).equals("GET")){
                if (requestUrl.startsWith("/css") || requestUrl.startsWith("/js")){
                    requestUrl = "./static" + requestUrl;
                    body = FileIoUtils.loadFileFromClasspath(requestUrl);
                    String[] urlSplitByDot = requestParser.splitDot(requestUrl);
                    response200Header(dos, body.length, urlSplitByDot[urlSplitByDot.length-1]);
                    responseBody(dos, body);
                    return;
                }
                if (requestUrl.startsWith("/") && requestUrl.endsWith("html")){
                    requestUrl = "./templates" + requestUrl;
                    body = FileIoUtils.loadFileFromClasspath(requestUrl);
                    String[] urlSplitByDot = requestParser.splitDot(requestUrl);
                    response200Header(dos, body.length, urlSplitByDot[urlSplitByDot.length-1]);
                    responseBody(dos, body);
                    return;
                }
                if (requestUrl.equals("/")){
                    body = "Hello world".getBytes();
                    response200Header(dos, body.length, "html");
                    responseBody(dos, body);
                    return;
                }
                while (!"".equals(s)) {
                    s = br.readLine();
                }
            }
            if (requestMethod.equals("POST")){
                while (requestParser.getContentLength(s) != -1) {
                    s = br.readLine();
                }
                if (s.length() == 0) {
                    return;
                }
                String requestBody = IOUtils.readData(br, requestParser.getContentLength(s));
                User user = requestParser.getUserInfo(requestBody);
                DataBase.addUser(user);
                response302Header(dos, "/index.html");
            }
        } catch (IOException | URISyntaxException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            /*
                HTTP/1.1 302 Found
                Location: http://www.iana.org/domains/example/
             */
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + type + ";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
