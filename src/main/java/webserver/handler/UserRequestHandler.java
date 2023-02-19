package webserver.handler;

import http.*;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class UserRequestHandler implements UrlMappingHandler {

    private static final String URL_MAPPING_REGEX = "/user/.+";
    private static final String TEMPLATES_FILEPATH = "./templates";

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        if ((httpRequest.getURL().equals("/user/list.html") || httpRequest.getURL().equals("/user/profile.html")) &&
                !isLoginUser(httpRequest)) {
            redirectToLoginPage(httpResponse);
            return;
        }

        byte[] bytes;
        try {
            bytes = FileIoUtils.loadFileFromClasspath(TEMPLATES_FILEPATH + httpRequest.getURL());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }

        httpResponse.setStatus(HttpStatus.OK);
        httpResponse.addHeaders(generateHeaders(httpRequest, bytes));
        httpResponse.setBody(bytes);
    }

    @Override
    public boolean support(HttpRequest httpRequest) {
        return httpRequest.getMethod() == HttpMethod.GET;
    }

    @Override
    public String getUrlMappingRegex() {
        return URL_MAPPING_REGEX;
    }

    private boolean isLoginUser(HttpRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        if (session == null) {
            return false;
        }
        Object attribute = session.getAttribute("user");
        return session.isValidate() && attribute != null;
    }

    private void redirectToLoginPage(HttpResponse httpResponse) {
        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setHeaders(new HttpHeaders(Map.of(HttpHeaders.LOCATION, List.of("/user/login.html"))));
    }

    private HttpHeaders generateHeaders(HttpRequest httpRequest, byte[] body) {
        HttpHeaders headers = new HttpHeaders();

        String url = httpRequest.getURL();
        int extensionIndex = url.lastIndexOf(".") + 1;
        String extension = url.substring(extensionIndex);

        headers.setHeader(HttpHeaders.CONTENT_TYPE,
                List.of(HttpContentType.fromExtensionAndCharset(extension, StandardCharsets.UTF_8)));

        headers.setHeader(HttpHeaders.CONTENT_LENGTH, List.of(String.valueOf(body.length)));

        return headers;
    }
}
