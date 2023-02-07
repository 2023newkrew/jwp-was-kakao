package webserver;

import webserver.handler.Handlers;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.io.RequestReader;
import webserver.io.ResponseWriter;

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
    //
    //    private Response handle(Request request) {
    //        if (controllers.isControllable(request)) {
    //            return controllers.control(request);
    //        }
    //
    //        if (resolvers.isResolvable(request)) {
    //            var content = resolvers.resolve(request);
    //            return new Response(HttpStatus.OK, content);
    //        }
    //
    //        throw new RuntimeException();
    //    }
}
