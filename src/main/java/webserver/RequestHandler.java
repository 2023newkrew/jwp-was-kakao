package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;
import webserver.view.ViewResolver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    private String PREFIX = "./templates";

    private ViewResolver viewResolver = new ViewResolver(PREFIX);

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                     connection.getPort()
        );

        try (
                InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream()
        ) {
            Request request = new Request(in);
            byte[] view = viewResolver.resolveByPath(request.getPath());
            Response response = new Response(HttpStatus.OK, view);
            writeResponse(out, response);
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void writeResponse(OutputStream out, Response response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.write(response.getBytes());
    }
}
