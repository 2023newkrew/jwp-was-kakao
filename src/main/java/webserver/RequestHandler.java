package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Arrays;
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

            String requestBody = "";
            if (tokens[0].equals("POST")) {
                requestBody = br.readLine();
            }

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body;
            if (url.equals("/")) {
                body = "Hello world".getBytes();
                response200Header(dos, body.length);
            } else if (url.contains(".html")) {
                body = FileIoUtils.loadFileFromClasspath("./templates" + url);
                response200Header(dos, body.length);
            } else if (url.contains(".css")) {
                body = FileIoUtils.loadFileFromClasspath("./static" + url);
                response200Header(dos, body.length);
            } else if (url.startsWith("/user/create") && tokens[0].equals("GET")) {
                body = "".getBytes();
                String query = url.split("\\?")[1];
                Map<String, String> fields = new HashMap<>();
                Arrays.stream(query.split("&")).forEach(field -> fields.put(field.split("=")[0], field.split("=")[1]));
                User user = new User(
                        fields.get("userId"),
                        fields.get("password"),
                        fields.get("name"),
                        fields.get("email")
                );
                DataBase.addUser(user);
                response201Header(dos, "/user/create", user.getUserId());
            } else if (url.startsWith("/user/create") && tokens[0].equals("POST")) {
                body = "".getBytes();
                Map<String, String> fields = new HashMap<>();
                Arrays.stream(requestBody.split("&")).forEach(field -> fields.put(field.split("=")[0], field.split("=")[1]));
                User user = new User(
                        fields.get("userId"),
                        fields.get("password"),
                        fields.get("name"),
                        fields.get("email")
                );
                DataBase.addUser(user);
                response201Header(dos, "/user/create", user.getUserId());
            } else {
                body = "Hello world".getBytes();
                response200Header(dos, body.length);
            }

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

    private void response201Header(DataOutputStream dos, String url, String id) {
        try {
            String location = url.split("\\?")[0];
            dos.writeBytes("HTTP/1.1 201 CREATED \r\n");
            dos.writeBytes("Location: " + location + "/" + id + " \r\n");
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
