package webserver;

import http.*;
import common.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.controller.Controller;
import web.controller.Controllers;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Controllers controllers;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controllers = new Controllers();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             OutputStream out = connection.getOutputStream()) {
            RequestInfo requestInfo = parseRequestInfo(br.readLine());
            Headers headers = new Headers();

            while(true){
                String line = br.readLine();
                if(Objects.isNull(line) || !"".equals(line)) {
                    break;
                }

                line = line.substring(0, line.indexOf(" \r\n"));
                headers.put(line);
            }

            HttpRequest httpRequest = new HttpRequest(requestInfo, headers);
            HttpResponse httpResponse = executeLogic(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);
            doResponse(dos, httpResponse.toByte());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse executeLogic(HttpRequest httpRequest) {
        Controller controller = controllers.getController(httpRequest);
        return controller.run(httpRequest);
    }

    private RequestInfo parseRequestInfo(String rawRequestInfo) {
        String[] requestInfo = rawRequestInfo.split(" ");
        HttpMethod httpMethod = HttpMethod.valueOf(requestInfo[0]);
        HttpUrl url = new HttpUrl(requestInfo[1]);
        Protocol protocol = Protocol.from(requestInfo[2]);

        return new RequestInfo(httpMethod, url, protocol);
    }

    private void doResponse(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
