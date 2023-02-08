package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.controller.StaticController;
import webserver.controller.UserSaveController;
import webserver.controller.ViewController;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Map<String, Controller> controllerMap = new HashMap<>();
    private final Controller viewController = new ViewController();
    private final Controller staticController = new StaticController();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllerMap.put("/user/create", new UserSaveController());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream(); BufferedReader br = new BufferedReader(
                new InputStreamReader(in))) {
            RequestParser requestParser = new RequestParser(br);

            HttpRequest httpRequest = requestParser.buildHttpRequest();

            Controller controller = handleControllerMapping(httpRequest);
            String viewName = controller.process(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);

            if (controller.isRedirectRequired()) {
                responseRedirectHome(dos);
                return;
            }
            sendBody(httpRequest, viewName, dos);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private Controller handleControllerMapping(HttpRequest httpRequest) {
        if (httpRequest.isStaticRequest()) {
            return this.staticController;
        }
        return this.controllerMap.getOrDefault(httpRequest.getUrl(), this.viewController);
    }

    private void responseRedirectHome(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Content-Type: text/html; charset=utf-8 \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendBody(HttpRequest httpRequest, String viewName, DataOutputStream dos)
            throws IOException, URISyntaxException {

        byte[] body = FileIoUtils.loadFileFromClasspath(viewName);
        response200Header(dos, httpRequest.getContentType(), body.length);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "; charset=utf-8 \r\n");
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
