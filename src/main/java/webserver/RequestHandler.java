package webserver;

import db.DataBase;
import http.HttpRequest;
import http.HttpRequestParser;
import http.HttpResponse;
import http.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

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
            HttpRequest request = HttpRequestParser.parse(in);
            DataOutputStream dos = new DataOutputStream(out);

            if(request.getMethod().equals("GET")) {
                doGet(request, dos);
            }

            if(request.getMethod().equals("POST")) {
                doPost(request, dos);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void doPost(HttpRequest request, DataOutputStream dos) throws IOException {

        if(request.getUri().getPath().equals("/user/create")) {
            String query = request.getBody();
            User user = User.fromQueryString(query);

            DataBase.addUser(user);

            HttpResponse response = new HttpResponse.Builder()
                    .status(HttpStatus.FOUND)
                    .addAttribute("Location", "/index.html")
                    .build();

            response(dos, response.getBytes());
        }
    }

    private void doGet(HttpRequest request, DataOutputStream dos) throws IOException, URISyntaxException {
        byte[] body = null;
        if (request.getUri().getPath().endsWith(".css")) {
            body = FileIoUtils.loadFileFromClasspath("static" + request.getUri().getPath());

            HttpResponse response = new HttpResponse.Builder()
                    .addAttribute("Content-Type", "text/css;charset=utf-8")
                    .body(body)
                    .build();

            response(dos, response.getBytes());
        }

        if(request.getUri().getPath().endsWith(".html")) {
            body = FileIoUtils.loadFileFromClasspath("templates" + request.getUri().getPath());
            HttpResponse response = new HttpResponse.Builder()
                    .addAttribute("Content-Type", "text/html;charset=utf-8")
                    .body(body)
                    .build();
            response(dos, response.getBytes());
        }

        if(request.getUri().getPath().equals("/")) {
            body = "Hello world".getBytes();
            HttpResponse response = new HttpResponse.Builder()
                    .addAttribute("Content-Type", "text/html;charset=utf-8")
                    .body(body)
                    .build();
            response(dos, response.getBytes());
        }

        if(request.getUri().getPath().equals("/user/create")) {
            String query = request.getUri().getQuery();
            User user = User.fromQueryString(query);

            DataBase.addUser(user);

            HttpResponse response = new HttpResponse.Builder()
                    .addAttribute("Content-Type", "text/html;charset=utf-8")
                    .build();

            response(dos, response.getBytes());
        }

    }

    private void response(DataOutputStream dos, byte[] data) {
        try {
            dos.write(data, 0, data.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
