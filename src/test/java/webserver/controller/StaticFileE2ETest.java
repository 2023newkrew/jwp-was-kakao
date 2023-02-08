package webserver.controller;

import org.junit.jupiter.api.Test;
import webserver.testsupport.DefaultBaseE2ETest;
import webserver.utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class StaticFileE2ETest extends DefaultBaseE2ETest {
    @Test
    void template_html_파일_요청() throws IOException, URISyntaxException {
        // given
        String socketInput = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        // when
        String socketOutput = getSocketOutputFromWebServer(socketInput);

        // then
        String expectedFirstLine = "HTTP/1.1 200 OK";
        String expectedContentTypeHeader = "Content-Type: text/html;charset=utf-8";
        String expectedContentLengthHeader = "Content-Length: 6816";
        String expectedBody = new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socketOutput)
                .startsWith(expectedFirstLine)
                .contains(expectedContentTypeHeader)
                .contains(expectedContentLengthHeader)
                .endsWith(expectedBody);
    }
}
