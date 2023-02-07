package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private final static Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String TEMPLATE_PATH = "./templates";
    private static final String STATIC_PATH = "./static";
    public static final String LINE_SEPARATOR = "\r\n";

    private final DataOutputStream dos;

    private final HttpHeader httpHeader = new HttpHeader();

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void forward(String path) {
        byte[] body = FileIoUtils.loadFileFromClasspath(getResourcePath(path));
        logger.info("Forward to file : {}", getResourcePath(path));
        writeHeader(ResponseStatus.OK, body.length, path);
        responseBody(body);
        flush();
    }

    public void sendRedirect(String path) {
        writeHeader(ResponseStatus.FOUND, 0, path);
        flush();
    }

    private String getResourcePath(String path) {
        if (path.endsWith(ContentType.TEXT_HTML.getExtension())) {
            return TEMPLATE_PATH + path;
        }
        return STATIC_PATH + path;
    }

    private void writeHeader(ResponseStatus responseStatus, int lengthOfBodyContent, String path) {
        writeLine("HTTP/1.1 " + responseStatus.toString());
        writeLine("Location: " + path);
        writeLine("Content-Type: "+ ContentType.of(path).getValue() + ";charset=utf-8");
        writeLine("Content-Length: " + lengthOfBodyContent);
        writeLine("");
    }

    private void writeLine(String line) {
        try {
            dos.writeBytes(line + LINE_SEPARATOR);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void flush() {
        try {
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
