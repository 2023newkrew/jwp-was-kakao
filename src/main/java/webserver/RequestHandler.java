package webserver;

import constant.HttpMethod;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.ResponseUtils;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Handler;
import java.util.stream.Collectors;

import static constant.DefaultConstant.*;
import static constant.PathConstant.*;
import static utils.FileIoUtils.*;
import static utils.RequestBuilder.*;
import static utils.ResponseUtils.*;
import static webserver.FrontController.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             DataOutputStream dos = new DataOutputStream(out);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

            HttpRequest httpRequest = getHttpRequest(bufferedReader);
            FrontController.handleRequest(httpRequest, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
