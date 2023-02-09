package webserver;

import controller.FrontController;
import controller.NotFoundException;
import controller.RedirectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        HttpRequest httpRequest = null;
        HttpResponse httpResponse = null;

        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = connection.getOutputStream()) {

            httpRequest = new HttpRequest(inputStream);
            httpResponse = new HttpResponse(outputStream);

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
