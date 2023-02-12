package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpResponse;
import webserver.http.header.HttpHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class ResponseWriter {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String CRLF = "\r\n";

    public void sendResponse(HttpResponse response, DataOutputStream dos) {
        switch (response.getStatus()) {
            case OK:
                response200Header(dos, response.getBody().length);
                break;
            case FOUND:
                response302Header(dos);
                break;
            case BAD_REQUEST:
                response400Header(dos);
                break;
            case NOT_FOUND:
                response404Header(dos);
                break;
            case METHOD_NOT_ALLOWED:
                response405Header(dos);
                break;
        }
        responseCommonHeader(dos, response);
        responseBody(dos, response.getBody());
    }

    private void responseCommonHeader(final DataOutputStream dos, final HttpResponse response) {
        Map<String, HttpHeader> headers = response.getHeaders();
        headers.keySet().forEach(headerName -> {
            try {
                dos.writeBytes(headerName + ": " + headers.get(headerName).getValue() + " " + CRLF);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        });
        try {
            dos.writeBytes(CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK " + CRLF);
            dos.writeBytes("Content-Type: text/html;charset=utf-8 " + CRLF);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " " + CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found " + CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response400Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request " + CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found " + CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response405Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 405 Method Not Allowed " + CRLF);
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
