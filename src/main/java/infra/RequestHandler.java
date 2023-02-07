package infra;

import infra.http.request.HttpRequest;
import infra.http.response.HttpResponse;
import infra.http.response.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestParser;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Router router;

    public RequestHandler(Socket connectionSocket, Router router) {
        this.connection = connectionSocket;
        this.router = router;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequest request = RequestParser.parse(br);
            if (request == null) {
                this.sendResponse(dos, new HttpResponse(ResponseStatus.BAD_REQUEST));
                return;
            }
            Controller controller = this.router.controller(request.getUri());
            this.sendResponse(dos, controller.response(request));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, HttpResponse response) throws IOException {
        byte[] output = response.flat();
        dos.write(output, 0, output.length);
        dos.flush();
    }
}
