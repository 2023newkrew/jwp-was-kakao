package webserver;

import servlet.ServletContainer;
import http.HttpMethod;
import http.Uri;
import http.request.Request;
import http.request.RequestBody;
import http.request.RequestHeaders;
import http.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;
import utils.ParsingUtils;

import java.io.*;
import java.net.Socket;
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
            Response response = servletContainer.dispatch(request);
            DataOutputStream dos = new DataOutputStream(out);
            sendResponse(response, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Request createRequest(InputStream in) throws IOException {
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in));

        Map<String, String> startLine = ParsingUtils.parseStartLine(IOUtils.readStartLine(bufferReader));
        HttpMethod method = HttpMethod.valueOf(startLine.get("method").toUpperCase());
        Uri uri = new Uri(startLine.get("uri"));
        String version = startLine.get("version");

        Map<String, String> headers = ParsingUtils.parseHeader(IOUtils.readRequestHeader(bufferReader));
        RequestHeaders requestHeaders = new RequestHeaders(headers);

        RequestBody requestBody = null;
        if (requestHeaders.get("Content-Length").isPresent()) {
            String body = IOUtils.readData(bufferReader, Integer.parseInt(requestHeaders.get("Content-Length").get()));
            requestBody = RequestBody.fromQueryString(body);
        }

        return new Request(method, uri, version, requestHeaders, requestBody);
    }

    private void sendResponse(Response response, DataOutputStream dos) {
        try {
            dos.writeBytes(response.getStatusLine() + " \r\n");
            dos.writeBytes(response.getHeaders());
            dos.writeBytes("\r\n");
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
