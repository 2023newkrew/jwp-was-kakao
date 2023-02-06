package webserver;

import db.DataBase;
import model.User;
import org.springframework.http.HttpStatus;
import webserver.request.Path;
import webserver.request.Request;
import webserver.resolver.ContentResolver;
import webserver.resolver.ContentResolvers;
import webserver.response.Response;
import webserver.response.ResponseHeader;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestHandler implements Runnable {

    private final Socket connection;

    public RequestHandler(Socket connectionSocket, List<ContentResolver> resolvers) {
        this.connection = connectionSocket;
        this.resolver = new ContentResolvers(resolvers);
    }

    private final ContentResolvers resolver;

    public void run() {
        try (
                InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream()
        ) {
            Request request = new Request(readRequest(in));
            Response response = handle(request);
            writeResponse(out, response);
        }
        catch (IOException ignore) {
        }
    }

    private List<String> readRequest(InputStream in) throws IOException {
        var br = new BufferedReader(new InputStreamReader(in));
        List<String> request = new ArrayList<>();
        String line;
        do {
            line = br.readLine();
            request.add(line);
            System.out.println(line);
        }
        while (Objects.nonNull(line) && !line.equals(""));
        return request;
    }

    private Response handle(Request request) {
        Path path = request.getPath();
        if (resolver.isResolvable(path)) {
            return createResolvedResponse(path);
        }

        if (path.isCreate()) {
            createUser(path);
            return new Response(new ResponseHeader(HttpStatus.OK));
        }

        throw new RuntimeException();
    }

    private Response createResolvedResponse(Path path) {
        var content = resolver.resolve(path);
        var header = new ResponseHeader(HttpStatus.OK, content);
        return new Response(header, content);
    }

    private void createUser(Path path) {
        var pathVariable = path.getPathVariable();
        var user = new User(
                pathVariable.get("userId"),
                pathVariable.get("password"),
                pathVariable.get("name"),
                pathVariable.get("email")
        );
        DataBase.addUser(user);
    }

    private static void writeResponse(OutputStream out, Response response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.write(response.getBytes());
    }
}
