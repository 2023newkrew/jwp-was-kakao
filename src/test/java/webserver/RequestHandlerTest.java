package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;
import webserver.handler.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    HandlerMappings handlerMappings;

    @BeforeEach
    void setUp() {
        initHandlerMappings();
    }

    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, handlerMappings);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: " +  FileIoUtils.loadFileFromClasspath("templates/index.html").length + "\r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    private void initHandlerMappings() {
        handlerMappings = new HandlerMappings();

        List<UrlMappingHandler> urlMappingHandlers = List.of(
                new HomeRequestHandler(),
                new QnaRequestHandler(),
                new UserCreateRequestHandler(),
                new UserRequestHandler(),
                new UserLoginRequestHandler()
        );

        urlMappingHandlers.forEach(
                handler -> handlerMappings.addUrlMappingHandler(handler));
    }
}