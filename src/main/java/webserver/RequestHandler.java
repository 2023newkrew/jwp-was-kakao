package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import logics.Controller;
import utils.requests.HttpRequest;
import utils.requests.HttpRequestVersion1;
import utils.response.HttpResponseVersion1;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            proceed(br, dos);
        } catch (IOException e) { // This IOException is related to connection. So cannot respond to client
            e.printStackTrace();
        }
    }

    private void proceed(BufferedReader br, DataOutputStream dos) throws IOException{
        try{
            HttpRequest httpRequest = HttpRequestVersion1.readFrom(br);
            Controller controller = new Controller();
            controller.makeResponse(httpRequest).respond(dos);
        } catch(RuntimeException e){ // If RuntimeException Occurs,
            new HttpResponseVersion1().setResponseCode(400).respond(dos); // Respond 400(Bad Request)
        }
    }
}
