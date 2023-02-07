package utils;

import lombok.experimental.UtilityClass;
import model.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

@UtilityClass
public class ResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

    public void response200Header(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");

            writeResponseHeader(dos, response);

            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    public void response302Header(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");

            writeResponseHeader(dos, response);

            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeResponseHeader(DataOutputStream dos, HttpResponse response) throws IOException {
        for (Map.Entry<String, String> entry : response.getHeaderEntrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
        }
    }
}
