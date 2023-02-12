package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.filter.ApplicationFilterChain;
import webserver.filter.ApplicationFilterConfig;
import webserver.filter.FilterChain;
import webserver.handler.Handler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    private final ResponseWriter responseWriter = new ResponseWriter();

    public RequestHandler(final Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            /* HttpRequest, HttpResponse 생성 */
            HttpResponse response = new HttpResponse();
            HttpRequest request = RequestParser.parseRequestMessage(reader, response);

            /* SessionFilter, LoginFilter 처리 */
            FilterChain chain = new ApplicationFilterChain(new ApplicationFilterConfig());
            chain.doFilter(request, response);

            /* Request를 parsing하거나 filtering 중 Response Status가 결정된 경우 */
            if (Objects.nonNull(response.getStatus())) {
                responseWriter.sendResponse(response, dos);
                return;
            }

            /* 요청을 처리 후 응답을 socket에 write */
            String path = request.getPath();

            HandlerMapping handlerMapping = new HandlerMapping();
            Handler handler = handlerMapping.getHandler(path);

            if (Objects.isNull(handler)) { /* 매치되는 Handler가 없을 경우 */
                if (path.equals("/")) {
                    response.setBody("Hello world".getBytes());
                } else { /* file 요청 처리 */
                    response.setBody(RequestParser.getFileContent(request.getPath(), response));
                }
                if (Objects.isNull(response.getStatus())) {
                    response.setStatus(HttpStatus.OK);
                }
            } else { /* 매치되는 Handler가 있을 경우 */
                handler.service(request, response);
            }
            responseWriter.sendResponse(response, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
