package webserver;

import org.springframework.http.HttpStatus;
import webserver.request.Request;
import webserver.response.MediaType;
import webserver.response.Response;
import webserver.view.Prefix;
import webserver.view.View;
import webserver.view.ViewResolver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class RequestHandler implements Runnable {

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    private List<Prefix> prefixes = List.of(
            new Prefix("./templates", MediaType.TEXT_HTML),
            //Todo: css를 직접 구분해야함
            new Prefix("./static", MediaType.TEXT_CSS)
    );

    private ViewResolver viewResolver = new ViewResolver(prefixes);

    public void run() {
        try (
                InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream()
        ) {
            Request request = new Request(in);
            View view = viewResolver.resolveByPath(request.getPath());
            Response response = new Response(HttpStatus.OK, view);
            writeResponse(out, response);
        }
        catch (IOException ignore) {
        }
    }

    private static void writeResponse(OutputStream out, Response response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.write(response.getBytes());
    }
}
