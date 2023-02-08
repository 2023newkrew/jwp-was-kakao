package webserver;

import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseSender {

    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);

    public static final String LINE_SEPARATOR = "\r\n";
    private final DataOutputStream dos;
    private final HttpResponse response;

    public ResponseSender(DataOutputStream dos, HttpResponse response) {
        this.dos = dos;
        this.response = response;
    }

    public void forward() {
        writeHeader();
        writeBody();
        flush();
    }

    private void writeHeader() {
        writeLine(response.getStatusLine());
        for (String headerString : response.getHeaderStrings()) {
            writeLine(headerString);
        }
        writeLine("");
    }

    private void writeLine(String line) {
        try {
            dos.writeBytes(line + LINE_SEPARATOR);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeBody() {
        byte[] body = response.getBody();
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
