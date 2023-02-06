package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import logics.Controller;
import utils.requests.HttpRequest;
import utils.requests.HttpRequestVersion1;

import java.io.*;
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
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            proceed(br, dos);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void proceed(BufferedReader br, DataOutputStream dos) throws IOException{
        HttpRequest httpRequest = HttpRequestVersion1.readFrom(br);
        Controller controller = new Controller();
        controller.makeResponse(httpRequest).respond(dos);
    }
}
