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
import model.RequestInfo;
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
            RequestInfo requestInfo = getRequestInfo(bufferedReader);
            requestAdapter.mapHandler(requestInfo, new DataOutputStream(out));
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private RequestInfo getRequestInfo(BufferedReader bufferedReader) throws IOException {
        RequestInfo requestInfo = new RequestInfo(bufferedReader.readLine());
        getRequestHeader(requestInfo, bufferedReader);
        getRequestBody(requestInfo, bufferedReader);
        return requestInfo;
    }

    private void getRequestHeader(RequestInfo requestInfo, BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        while (!"".equals(line) && Objects.nonNull(line)) {
            System.out.println(line);
            requestInfo.readNextLine(line);
            line = bufferedReader.readLine();
        }
    }

    private void getRequestBody(RequestInfo requestInfo, BufferedReader bufferedReader) throws IOException {
        String bodyLengthString = requestInfo.getHeaderValue("Content-Length");
        if (Objects.nonNull(bodyLengthString)) {
            String body = IOUtils.readData(bufferedReader, Integer.parseInt(bodyLengthString));
            requestInfo.setBodyParams(body);
        }
    }
}

