package web;

import error.ApplicationException;
import error.ErrorType;
import http.Body;
import http.HttpHeaders;
import http.Protocol;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.request.HttpUrl;
import http.request.RequestInfo;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;
import web.controller.Controller;
import web.controller.Controllers;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

import static error.ErrorType.*;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Controllers controllers;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controllers = new Controllers();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            RequestInfo requestInfo = createRequestInfo(br.readLine());
            HttpHeaders httpHeaders = createHeader(br);
            Body body = createBody(br, httpHeaders);

            HttpRequest httpRequest = new HttpRequest(requestInfo, httpHeaders, body);
            HttpResponse httpResponse = doService(httpRequest);

            doResponse(dos, httpResponse.toByte());
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ApplicationException(BUFFER_READ_FAILED, e.getMessage());
        }
    }

    private void doResponse(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ApplicationException(DATA_WRITE_FAILED, e.getMessage());
        }
    }

    private HttpResponse doService(HttpRequest httpRequest) {
        Controller controller = controllers.getController(httpRequest);
        return controller.run(httpRequest);
    }

    private Body createBody(BufferedReader br, HttpHeaders httpHeaders) throws IOException {
        if (!httpHeaders.containsKey("Content-Length")) {
            return Body.empty();
        }

        return new Body(IOUtils.readData(br, Integer.parseInt(httpHeaders.get("Content-Length"))));
    }

    private HttpHeaders createHeader(BufferedReader br) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();

        while (true) {
            String line = br.readLine();
            if (Objects.isNull(line) || "".equals(line)) {
                break;
            }

            httpHeaders.put(line.trim());
        }

        return httpHeaders;
    }

    private RequestInfo createRequestInfo(String rawRequestInfo) {
        String[] requestInfo = rawRequestInfo.split(" ");
        HttpMethod httpMethod = HttpMethod.valueOf(requestInfo[0]);
        HttpUrl url = new HttpUrl(requestInfo[1]);
        Protocol protocol = Protocol.from(requestInfo[2]);

        return new RequestInfo(httpMethod, url, protocol);
    }
}
