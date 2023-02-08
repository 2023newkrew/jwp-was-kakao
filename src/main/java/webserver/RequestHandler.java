package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import supports.HttpParser;
import utils.IoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.info("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String httpRequest = IoUtils.getHttpRequest(br);

            HttpParser httpParser = new HttpParser(httpRequest);
            String path = httpParser.getPath();

            PathBinder pathBinder = new PathBinder();
            pathBinder.bind(path, out, br, httpParser);

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }
}
