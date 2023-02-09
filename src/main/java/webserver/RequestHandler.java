package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final RequestAdapter requestAdapter = new RequestAdapter();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Request request = getRequest(bufferedReader);
            Response response = new Response(new DataOutputStream(out));
            checkOrCreateSession(request, response);
            requestAdapter.mapHandler(request, response);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private void checkOrCreateSession(Request request, Response response) {
        String sessionId = request.getCookie("JSESSIONID");
        if (Objects.nonNull(sessionId) && Objects.nonNull(SessionManager.findSession(sessionId))) {
            return;
        }
        Session newSession = new Session(UUID.randomUUID().toString());
        SessionManager.add(newSession.getId(), newSession);
        request.setSession(newSession);
        response.setCookie("JSESSIONID", newSession.getId() + "; Path=/");
    }

    private Request getRequest(BufferedReader bufferedReader) throws IOException {
        Request request = new Request(bufferedReader.readLine());
        getRequestHeader(request, bufferedReader);
        getRequestBody(request, bufferedReader);
        return request;
    }

    private void getRequestHeader(Request request, BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        while (!"".equals(line) && Objects.nonNull(line)) {
            System.out.println(line);
            request.readNextLine(line);
            line = bufferedReader.readLine();
        }
    }

    private void getRequestBody(Request request, BufferedReader bufferedReader) throws IOException {
        String bodyLengthString = request.getHeaderValue("Content-Length");
        if (Objects.nonNull(bodyLengthString)) {
            String body = IOUtils.readData(bufferedReader, Integer.parseInt(bodyLengthString));
            request.setBodyParams(body);
        }
    }
}

