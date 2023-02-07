package webserver.support;

import http.HttpResponse;
import http.HttpStatus;
import http.exception.HttpRequestFormatException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GlobalExceptionHandler {

    private final Socket connection;

    public GlobalExceptionHandler(Socket connection) {
        this.connection = connection;
    }

    public void handle(Exception exception) {
        if (exception instanceof IllegalArgumentException || exception instanceof HttpRequestFormatException) {
            responseBadRequest();
        } else {
            responseInternalServerError();
        }
    }

    private void responseBadRequest() {
        try (DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream())){
            HttpResponse httpResponse =
                    HttpResponse.HttpResponseBuilder.aHttpResponse()
                            .withVersion("HTTP/1.1")
                            .withStatus(HttpStatus.BAD_REQUEST)
                            .build();
            dataOutputStream.write(httpResponse.toBytes());
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void responseInternalServerError() {
        try (DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream())){
            HttpResponse httpResponse =
                    HttpResponse.HttpResponseBuilder.aHttpResponse()
                            .withVersion("HTTP/1.1")
                            .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build();
            dataOutputStream.write(httpResponse.toBytes());
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
