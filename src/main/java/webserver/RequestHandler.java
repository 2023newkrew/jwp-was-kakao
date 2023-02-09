package webserver;

import http.HttpMethod;
import http.HttpRequestHeader;
import http.HttpStartLine;
import http.Uri;
import http.request.Request;
import http.request.RequestBody;
import http.request.RequestHeaders;
import http.request.RequestStartLine;
import http.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.ServletContainer;
import utils.IOUtils;
import utils.RequestParsingUtils;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ServletContainer servletContainer;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.servletContainer = ServletContainer.getInstance();
    }

    /**
     * 요청을 가져와서 응답을 보내주는 역할만
     */
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = createRequest(in);
            Response response = servletContainer.serve(request);
            sendResponse(out, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private Request createRequest(InputStream in) throws IOException {
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in));

        String rawStartLine = IOUtils.readStartLine(bufferReader);
        RequestStartLine startLine = RequestStartLine.fromRawStartLine(rawStartLine);

        List<String> rawHeaders = IOUtils.readRequestHeader(bufferReader);
        RequestHeaders requestHeaders = RequestHeaders.fromRawHeaders(rawHeaders);

        RequestBody requestBody = null;
        if (requestHeaders.get(HttpRequestHeader.CONTENT_LENGTH).isPresent()) {
            String body = IOUtils.readData(bufferReader, Integer.parseInt(requestHeaders.get(HttpRequestHeader.CONTENT_LENGTH).get()));
            requestBody = RequestBody.fromQueryString(body);
        }

        return new Request(startLine, requestHeaders, requestBody);
    }

    private void sendResponse(OutputStream out, Response response) {
        response.send(new DataOutputStream(out));
    }
}
