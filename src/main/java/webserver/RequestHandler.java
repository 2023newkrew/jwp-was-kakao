package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import utils.FileIoUtils;

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
            HttpRequest httpRequest = new HttpRequest(bufferedReader);
            String requestPath = httpRequest.getTarget()
                    .getPath();

            if (httpRequest.getTarget()
                    .getMethod() == HttpMethod.POST) {
                String requestBody = httpRequest.getBody();
                requestBody = URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
                MultiValueMap<String, String> requestParams = UriComponentsBuilder.fromUriString(requestPath)
                        .query(requestBody)
                        .build()
                        .getQueryParams();

                if (requestPath.equals("/user/create")) {
                    addUser(requestParams);
                    HttpResponse response = new HttpResponse.Builder()
                            .setStatus(HttpStatus.OK)
                            .addHeader("Location", "http://localhost:8080/index.html")
                            .build();
                    response.send(dos);
                    return;
                }
            }
            if (httpRequest.getTarget()
                    .getMethod() == HttpMethod.GET) {
                byte[] body;
                try {
                    body = FileIoUtils.loadFileFromClasspath("./templates" + requestPath);
                } catch (NullPointerException e) {
                    body = FileIoUtils.loadFileFromClasspath("./static" + requestPath);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

                String contentType = Files.probeContentType(new File(requestPath).toPath());
                HttpResponse response = new HttpResponse.Builder()
                        .setStatus(HttpStatus.OK)
                        .addHeader("Content-Type", contentType)
                        .setBody(body)
                        .build();
                response.send(dos);
            }
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
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
}
