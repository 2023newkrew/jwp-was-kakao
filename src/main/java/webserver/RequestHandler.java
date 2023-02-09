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
import model.MyHttpRequest;
import model.MyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.controller.StaticController;
import webserver.controller.UserLoginController;
import webserver.controller.UserSaveController;
import webserver.controller.ViewController;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Map<String, Controller> controllerMap = new HashMap<>();
    private final Controller viewController = ViewController.getInstance();
    private final Controller staticController = StaticController.getInstance();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllerMap.put(UserSaveController.URL, UserSaveController.getInstance());
        controllerMap.put(UserLoginController.URL, UserLoginController.getInstance());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream(); BufferedReader br = new BufferedReader(
                new InputStreamReader(in))) {
            RequestParser requestParser = new RequestParser(br);

            MyHttpRequest httpRequest = requestParser.buildHttpRequest();

            Controller controller = handleControllerMapping(httpRequest);
            MyHttpResponse httpResponse = new MyHttpResponse();
            String viewName = controller.process(httpRequest, httpResponse);

            DataOutputStream dos = new DataOutputStream(out);
            sendResponse(dos, httpResponse, viewName);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private Controller handleControllerMapping(MyHttpRequest httpRequest) {
        if (httpRequest.isStaticRequest()) {
            return this.staticController;
        }
        return this.controllerMap.getOrDefault(httpRequest.getUrl(), this.viewController);
    }

    private void sendResponse(DataOutputStream dos, MyHttpResponse httpResponse, String viewName)
            throws IOException, URISyntaxException {
        dos.writeBytes(httpResponse.toString());

        if (!httpResponse.isRedirectRequired()) {
            byte[] body = FileIoUtils.loadFileFromClasspath(viewName);
            dos.writeBytes(String.format("Content-Length: %d\r%n", body.length));
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }
}
