package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import utils.FileIoUtils;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            if (line == null) {
                return;
            }
            String method = line.split(" ")[0];
            String requestedUri = line.split(" ")[1];

            if (method.equals("POST")) {
                int contentLength = 0;
                while (!"".equals(line)) {
                    if (line == null) {
                        break;
                    }
                    line = bufferedReader.readLine();
                    if (line.startsWith("Content-Length: ")) {
                        contentLength = Integer.parseInt(line.split(" ")[1]);
                    }
                }
                String requestBody = IOUtils.readData(bufferedReader, contentLength);
                requestBody = URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
                MultiValueMap<String, String> requestParams = UriComponentsBuilder.fromUriString(requestedUri)
                        .query(requestBody)
                        .build()
                        .getQueryParams();

                if (requestedUri.equals("/user/create")) {
                    addUser(requestParams);
                    response302Header(dos, "http://localhost:8080/index.html");
                    dos.flush();
                    return;
                }
            }

            byte[] body = "Hello world".getBytes();

            String[] pathSplit = requestedUri.split("\\.");
            String extension = pathSplit[pathSplit.length - 1];
            if (extension.equals("html") || extension.equals("ico")) {
                body = FileIoUtils.loadFileFromClasspath("./templates" + requestedUri);
            } else {
                body = FileIoUtils.loadFileFromClasspath("./static" + requestedUri);
            }

            String contentType = Files.probeContentType(new File(requestedUri).toPath());
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void addUser(MultiValueMap<String, String> requestParams) {
        String userId = requestParams.getFirst("userId");
        String password = requestParams.getFirst("password");
        String name = requestParams.getFirst("name");
        String email = requestParams.getFirst("email");
        User user = User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();
        DataBase.addUser(user);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + " \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
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
