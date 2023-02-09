package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String filePath) {
        try {
            Path path = Paths.get(filePath);
            String mimeType = Files.probeContentType(path);

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response200Header(DataOutputStream dos){
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response302Header(DataOutputStream dos, String redirectPath) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectPath + " \r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response404Header(DataOutputStream dos){
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response400Header(DataOutputStream dos){
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request \r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void setCookie(DataOutputStream dos, String key, String value){
        try {
            dos.writeBytes("Set-Cookie: "+ key + "=" + value + "; Path=/\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
