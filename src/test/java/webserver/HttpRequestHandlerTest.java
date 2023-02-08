package webserver;

import org.junit.jupiter.api.Test;
import support.StubSocket;
import webserver.domain.RoutingHandler;
import webserver.utils.FileIoUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestHandlerTest {
    @Test
    void socket_out() {
        // given
        final var socket = new StubSocket();
        final var router = new RoutingHandler();
        final var handler = new RequestHandler(socket, router);

        // when
        handler.run();

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 200 OK",
                "Content-Length: 11",
                "Content-Type: text/plain",
                "",
                "Hello world");

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void index() throws IOException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final var router = new RoutingHandler();
        final var handler = new RequestHandler(socket, router);

        // when
        handler.run();

        // then


        var expected = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 7159\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }
}
