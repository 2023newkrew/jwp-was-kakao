package webserver.handler.response;

import model.HttpRequest;
import webserver.handler.response.header.Response302Header;
import webserver.handler.response.body.ResponseBody;
import webserver.handler.response.header.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Optional;

public class PostResponseHandler implements ResponseHandler {
    private static PostResponseHandler postResponseHandler = new PostResponseHandler();

    private PostResponseHandler() {}

    public static PostResponseHandler getInstance() { return postResponseHandler; }

    @Override
    public void handle(HttpRequest httpRequest, OutputStream outputStream) throws IOException, URISyntaxException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        byte[] body = generateBody(httpRequest);
        Optional<String> accept = Optional.ofNullable(httpRequest.getHeader("Accept"));

        ResponseHeader responseHeader = Response302Header.getInstance();
        ResponseBody responseBody = ResponseBody.getInstance();

        responseHeader.generate(dataOutputStream, body.length, accept.orElse("text/html").split(",")[0]);
        responseBody.generate(dataOutputStream, body);
    }

    @Override
    public byte[] generateBody(HttpRequest httpRequest) throws IOException, URISyntaxException {
        return new byte[0];
    }
}
