package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;
import utils.requests.HttpRequest;
import utils.requests.HttpRequestVersion1;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * RequestHandler Coded with POP(Process-Oriented Programming, 절차지향).
 */
public class RequestHandlerPOP implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerPOP.class);

    private final Socket connection;

    public RequestHandlerPOP(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequest httpRequest = HttpRequestVersion1.readFrom(br);
            byte[] body;
            String s = br.readLine();
            String[] tokens = s.split(" "); // GET URL HTTP/1.1


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
                    requestUrl = "./static" + requestUrl;
                    body = FileIoUtils.loadFileFromClasspath(requestUrl);
                    String[] urlSplitByDot = requestUrl.split("\\.");
                    response200Header(dos, body.length, urlSplitByDot[urlSplitByDot.length-1]);
                    responseBody(dos, body);
                }
                else if (requestUrl.startsWith("/") && requestUrl.contains(".")){
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

                while (!"".equals(s)) {
                    s = br.readLine();
                }
            }
            else if (requestMethod.equals("POST")){
                int contentLength = -1;
                while (!"".equals(s)) {
                    s = br.readLine();
                    if ("Content-Length".equals(s.split(":")[0])){
                        contentLength = Integer.parseInt(s.split(":")[1].trim());
                    }
                }
                if (contentLength >= 0) {
                    String requestBody = IOUtils.readData(br, contentLength);
                    Map<String, String> bodyMap = new HashMap();
                    for (String splitted : requestBody.split("&")) {
                        bodyMap.put(splitted.split("=")[0], splitted.split("=")[1]);
                    }
                    User user = new User(bodyMap.get("userId"), bodyMap.get("password"),
                            bodyMap.get("name"), bodyMap.get("email"));
                    DataBase.addUser(user);
                    response302Header(dos, "/index.html");
                }
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
