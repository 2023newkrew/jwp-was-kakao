package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;

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

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            String line = br.readLine();

            if (line == null || "".equals(line)) {
                throw new RuntimeException("잘못된 요청 형식입니다.");
            }

            String[] tokens = line.split(" ");
            String url = tokens[1];
            line = br.readLine();

            while (!"".equals(line)) {
                if (line == null) { return; }
                String[] header = line.split(": ");
                headers.put(header[0], header[1]);
                line = br.readLine();
            }

            String requestBody = "";
            if (tokens[0].equals("POST")) {
                requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
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
            } else if (url.startsWith("/user/create")) {
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
                response302Header(dos, "/index.html");
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

    private void response302Header(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Location: " + redirectUrl + " \r\n");
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
