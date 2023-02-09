package webserver.handler;

import http.*;
import org.junit.jupiter.api.Test;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class UserRequestHandlerTest {

    @Test
    void handle() throws IOException, URISyntaxException {
        UrlMappingHandler userRequestHandler = new UserRequestHandler();

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setMethod(HttpMethod.GET);
        httpRequest.setURL("/user/form.html");

        String urlMappingRegex = userRequestHandler.getUrlMappingRegex();
        boolean urlMatches = Pattern.compile(urlMappingRegex).matcher(httpRequest.getURL()).matches();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setVersion("HTTP/1.1");
        userRequestHandler.handle(httpRequest, httpResponse);

        byte[] bytes = FileIoUtils.loadFileFromClasspath("./templates/user/form.html");

        assertThat(userRequestHandler.support(httpRequest)).isTrue();
        assertThat(urlMatches).isTrue();

        assertThat(httpResponse.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(httpResponse.getHeaders().getHeaders())
                .hasSize(2)
                .containsEntry(HttpHeaders.CONTENT_TYPE,
                        List.of(HttpContentType.fromExtensionAndCharset("html", StandardCharsets.UTF_8)))
                .containsEntry(HttpHeaders.CONTENT_LENGTH, List.of(String.valueOf(bytes.length)));
        assertThat(httpResponse.getBody()).isEqualTo(bytes);
    }
}