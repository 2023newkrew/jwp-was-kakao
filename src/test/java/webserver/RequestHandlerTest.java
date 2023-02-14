package webserver;

import webserver.controller.RequestHandler;
import org.junit.jupiter.api.Test;
import webserver.repository.MemorySessionRepository;
import webserver.repository.MemoryUserRepository;
import support.StubSocket;
import webserver.utils.FileIoUtils;
import webserver.controller.GlobalController;
import webserver.controller.RequestMappingHandler;
import webserver.service.SessionService;
import webserver.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    private final RequestMappingHandler requestMappingHandler = new RequestMappingHandler(new GlobalController(new UserService(new MemoryUserRepository()), new SessionService(new MemorySessionRepository())));
    @Test
    void socket_out() {
        // given
        final var socket = new StubSocket();
        final var handler = new RequestHandler(socket, requestMappingHandler);

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
        final RequestHandler handler = new RequestHandler(socket, requestMappingHandler);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 6897 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }
}