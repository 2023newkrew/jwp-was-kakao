package webserver;

import controller.BreakException;
import controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

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
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
            httpResponse.sendNotFound();
        } catch (BreakException e) {
            logger.debug("redirect");
        } catch (Exception e) {
            logger.error(e.getMessage());
            httpResponse.sendError(e);
        }
    }
}
