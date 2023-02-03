package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            String path = null;
            String root = "./templates";
            String textType = "html";
            while (!"".equals(line) && Objects.nonNull(line)) {
                System.out.println(line);
                String[] tokens = line.split(" ");
                if (Objects.nonNull(HttpMethod.resolve(tokens[0]))) {
                    path = tokens[1];
                    // TODO

                    String[] pathTokens = path.split("/");
                    String dir = pathTokens[1];
                    if(Objects.nonNull(Directory.resolve(dir.toUpperCase()))){
                        root = "./static";
                        textType = dir.toLowerCase();
                    }
                }
                line = bufferedReader.readLine();
            }
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body;
            if (Objects.nonNull(path)) {
                body = FileIoUtils.loadFileFromClasspath(root + path);
            } else {
                body = "Hello world".getBytes();
            }
            response200Header(dos, body.length, textType);
            responseBody(dos, body);
        } catch (IOException | URISyntaxException e) {
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

enum Directory {
    CSS, FONTS, IMAGES, JS;
    private static final Map<String, Directory> mappings = new HashMap<>(16);

    static {
        for (Directory directory : values()) {
            mappings.put(directory.name(), directory);
        }
    }

    @Nullable
    public static Directory resolve(@Nullable String dir) {
        return (dir != null ? mappings.get(dir) : null);
    }

}