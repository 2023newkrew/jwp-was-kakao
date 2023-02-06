package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import model.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import utils.FileIoUtils;
import utils.IOUtils;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream(); BufferedReader br = new BufferedReader(
                new InputStreamReader(in))) {
            String line = br.readLine();
            String[] tokens = line.split(" ");
            String httpMethod = tokens[0];
            String requestUrl = tokens[1];
            Map<String, String> queryParams = new HashMap<>();
            Map<String, String> headerMap = new HashMap<>();
            Map<String, String> requestBody = new HashMap<>();

            // url parsing
            if (requestUrl.contains("?")) {
                String queries = requestUrl.split("\\?")[1];
                requestUrl = requestUrl.split("\\?")[0];
                for (String query : queries.split("&")) {
                    String[] keyValueSet = query.split("=");
                    queryParams.put(keyValueSet[0], keyValueSet[1]);
                }
            }

            // header parsing
            while (!StringUtils.isEmpty(line)) {
                String[] headers = line.split(" ");
                headerMap.put(headers[0].substring(0, headers[0].length() - 1), headers[1]);
                line = br.readLine();
            }

            String requestBodyString;
            if (headerMap.containsKey("Content-Length")) {
                requestBodyString = IOUtils.readData(br, Integer.parseInt(headerMap.get("Content-Length")));
                for (String query : requestBodyString.split("&")) {
                    String[] keyValueSet = query.split("=");
                    requestBody.put(keyValueSet[0], keyValueSet[1]);
                }
            }

            HttpRequest httpRequest = new HttpRequest(httpMethod, requestUrl, queryParams, headerMap, requestBody);
            Map<String, String> userInfo = httpRequest.getQueryParams();

            User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"),
                    userInfo.get("email"));
            DataBase.addUser(user);

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body;
            // resolver
            try {
                if (httpRequest.getUrl().endsWith("html")) {
                    body = FileIoUtils.loadFileFromClasspath("./templates" + httpRequest.getUrl());
                } else {
                    body = FileIoUtils.loadFileFromClasspath("./static" + httpRequest.getUrl());
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }

            response200Header(dos, httpRequest.getContentType(), body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "; charset=utf-8 \r\n");
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
