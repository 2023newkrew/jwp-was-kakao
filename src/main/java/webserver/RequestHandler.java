package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.HttpParser;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String TEMPLATE_ROOT_PATH = "./templates";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (!"".equals(line) && line != null) {
                sb.append(line).append('\n');
                line = br.readLine();
            }

            HttpParser httpParser = new HttpParser(sb.toString());
            String path = httpParser.getPath();
            byte[] body = new byte[0];
            System.out.println(path);
            DataOutputStream dos = new DataOutputStream(out);
            if(path.endsWith("html") ||  path.endsWith("html/")){
                body = FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + path);
                response200Header(dos, body.length, path);
            } else if(path.startsWith("/user/create")){
                Integer contentLength = httpParser.getContentLength();
                String userBody = IOUtils.readData(br, contentLength);
                HashMap<String, String> queryParam = parseQueryParameter(userBody);
                
                User user = new User(
                        queryParam.get("userId"),
                        queryParam.get("password"),
                        queryParam.get("name"),
                        queryParam.get("email")
                );
                DataBase.addUser(user);
                response302Header(dos, "/index.html");
            } else {
                body = FileIoUtils.loadFileFromClasspath("./static" + path);
                response200Header(dos, body.length, path);
            }
            responseBody(dos, body);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private HashMap<String, String> parseQueryParameter(String userBody){
        HashMap<String, String> result = new HashMap<>();

        for(String info : userBody.split("\\?")){
            String key = info.split("=")[0];
            String value = info.split("=")[1];
            result.put(key, value);
        }

        return result;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String filePath) {
        try {
            Path path = Paths.get(filePath);
            String mimeType = Files.probeContentType(path);

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectPath) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectPath + " \r\n");
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
