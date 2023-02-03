package webserver;

import exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

import static utils.IOUtils.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        DataOutputStream dos;
        InputStream in;
        OutputStream out;
        try {
            in = connection.getInputStream();
            out = connection.getOutputStream();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return;
        }
        try {
            String path = extractPath(extractRequestFirstLine(in));
            byte[] body;

            if (path.equals("/")) {
                body = "Hello world".getBytes();
                dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
                return;
            }

            body = FileIoUtils.loadFileFromClasspath("./templates" + path);

            dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        }  catch (ResourceNotFoundException e) {
            dos = new DataOutputStream(out);
            response404NotFoundHeader(dos);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404NotFoundHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 NOT FOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
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
