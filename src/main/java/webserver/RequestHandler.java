package webserver;

import controller.FrontController;
import exception.NotFoundException;
import exception.RedirectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = connection.getOutputStream()) {

            handle(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle(InputStream inputStream, OutputStream outputStream) {
        HttpRequest httpRequest = new HttpRequest(inputStream);
        HttpResponse httpResponse = new HttpResponse(outputStream);

        try {
            FrontController frontController = new FrontController();
            frontController.service(httpRequest, httpResponse);

            httpResponse.sendResponse();
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            httpResponse.sendNotFound();
        } catch (RedirectException e) {
            logger.debug(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            httpResponse.sendError(e);
        }
    }
}
