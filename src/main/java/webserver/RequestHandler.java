package webserver;

import controller.DispatcherServlet;
import controller.MyController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.ResponseEntity;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private DispatcherServlet dispatcherServlet;
    private HttpRequest httpRequest;

    public RequestHandler(Socket connection, DispatcherServlet dispatcherServlet){
        this.dispatcherServlet = dispatcherServlet;
        this.connection = connection;
        this.httpRequest = new HttpRequest();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            httpRequest.parsingRequest(br);

            DataOutputStream dos = new DataOutputStream(out);

            // Handler Mapping (FrontController는 Dispatcher Servlet의 역할을 수행)
            MyController myController = dispatcherServlet.handlerMapping(httpRequest, dos);
            ResponseEntity responseEntity = myController.handle(httpRequest, dos);
            responseEntity.response(dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
