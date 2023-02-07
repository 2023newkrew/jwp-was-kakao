package webserver.handler.response;

import org.apache.commons.io.FilenameUtils;
import utils.FileIoUtils;
import model.HttpRequest;
import webserver.handler.response.header.Response200Header;
import webserver.handler.response.body.ResponseBody;
import webserver.handler.response.header.ResponseHeader;
import webserver.constants.path.FilePath;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Optional;

public class GetResponseHandler implements ResponseHandler {
    private static GetResponseHandler getResponseHandler = new GetResponseHandler();

    private GetResponseHandler() {}

    public static GetResponseHandler getInstance() { return getResponseHandler; }

    @Override
    public void handle(HttpRequest httpRequest, OutputStream outputStream) throws IOException, URISyntaxException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        byte[] body = generateBody(httpRequest);
        Optional<String> accept = Optional.ofNullable(httpRequest.getHeader("Accept"));

        ResponseHeader responseHeader = Response200Header.getInstance();
        ResponseBody responseBody = ResponseBody.getInstance();

        responseHeader.generate(dataOutputStream, body.length, accept.orElse("text/html").split(",")[0]);
        responseBody.generate(dataOutputStream, body);
    }

    @Override
    public byte[] generateBody(HttpRequest httpRequest) throws IOException, URISyntaxException {
        String uri = httpRequest.getUri();
        String extension = FilenameUtils.getExtension(uri);

        if (extension.isBlank()) {
            return "Hello world".getBytes();
        }

        String path = FilePath.getPathByExtension(extension);

        return FileIoUtils.loadFileFromClasspath(path + uri);
    }
}
