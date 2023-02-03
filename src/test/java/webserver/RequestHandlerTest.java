package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;
import utils.IOUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @Test
    void socket_out() {
        // given
        final var socket = new StubSocket();
        final var handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 11 ",
                "",
                "Hello world");

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then


        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 6943 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("InputStream으로 부터 requestFirstLine를 얻는다.")
    void extractRequestFirstLineTest(){
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.UTF_8));
        String path = IOUtils.extractRequestFirstLine(inputStream);
        assertThat(path).isEqualTo("GET /index.html HTTP/1.1 ");
    }

    @Test
    @DisplayName("requestFirstLine로부터 요청 경로를 얻는다.")
    void extractPathTest(){
        String requestFirstLine = "GET /index.html HTTP/1.1 ";
        String expected = "/index.html";
        assertThat(IOUtils.extractPath(requestFirstLine)).isEqualTo(expected);
    }
}