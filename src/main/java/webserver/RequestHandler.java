package webserver;

import controller.FrontController;
import model.http.CustomHttpRequest;
import model.http.CustomHttpRequestFactory;
import model.http.CustomHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            CustomHttpRequest request = CustomHttpRequestFactory.generateHttpRequest(br);
            CustomHttpResponse response = FrontController.getInstance().getHttpResponse(request);
            dos.writeBytes(response.getHttpStatus().getLine() + " \r\n");
            response.getHeaders().respond(dos);
            dos.writeBytes("\r\n");
            dos.write(response.getBody().getBytes(), 0, response.getBody().getBytes().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
    }
}
