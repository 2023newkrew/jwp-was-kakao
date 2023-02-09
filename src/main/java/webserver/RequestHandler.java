package webserver;

import webserver.handler.Handlers;
import webserver.io.RequestReader;
import webserver.io.ResponseWriter;
import webserver.request.Request;
import webserver.response.Response;

import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private final Socket connection;

    private final Handlers handlers;

    public RequestHandler(Socket connectionSocket, Handlers handlers) {
        this.connection = connectionSocket;
        this.handlers = handlers;
    }

    @Override
    public void run() {
        try (
                RequestReader requestReader = new RequestReader(connection.getInputStream());
                ResponseWriter responseWriter = new ResponseWriter(connection.getOutputStream())
        ) {

            Request request = requestReader.read();
            Response response = handlers.handle(request);
            responseWriter.write(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
