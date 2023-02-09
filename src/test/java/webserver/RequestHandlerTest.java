package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @DisplayName("GET /")
    @Test
    void socket_out() {
        final var request = "GET / HTTP/1.1 \r\n" +
                "Host: localhost:8080 \r\n" +
                "\r\n";

        final var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Length: 11 \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "\r\n" +
                "Hello world";

        testHandler(request, expected);
    }

    @DisplayName("GET /index.html")
    @Test
    void index() throws IOException, URISyntaxException {
        final var request = "GET /index.html HTTP/1.1 \r\n" +
                "Host: localhost:8080 \r\n" +
                "Connection: keep-alive \r\n" +
                "\r\n";

        final var expectedBody = FileIoUtils.loadFileFromClasspath("templates/index.html");
        final var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Length: " + expectedBody.length + " \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "\r\n" +
                new String(expectedBody);

        testHandler(request, expected);
    }

    @DisplayName("GET /css/styles.css")
    @Test
    void css() throws IOException, URISyntaxException {
        final var request = "GET /css/styles.css HTTP/1.1 \r\n" +
                "Host: localhost:8080 \r\n" +
                "Accept: text/css,*/*;q=0.1" +
                "Connection: keep-alive \r\n" +
                "\r\n";

        final var expectedBody = FileIoUtils.loadFileFromClasspath("static/css/styles.css");
        final var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Length: " + expectedBody.length + " \r\n" +
                "Content-Type: text/css \r\n" +
                "\r\n" +
                new String(expectedBody);

        testHandler(request, expected);
    }

    @DisplayName("POST /user/create")
    @Test
    void post() {
        final var requestBody = "userId=cu" +
                "&password=password" +
                "&name=%EC%9D%B4%EB%8F%99%EA%B7%9C" +
                "email=brainbackdoor%40gmail.com";
        final var request = "POST /user/create HTTP/1.1 \r\n" +
                "Host: localhost:8080 \r\n" +
                "Connection: keep-alive \r\n" +
                "Content-Length: " + requestBody.getBytes().length + " \r\n" +
                "Content-Type: application/x-www-form-urlencoded \r\n" +
                "Accept: */* \r\n" +
                "\r\n" +
                requestBody;

        final var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: /index.html \r\n" +
                "\r\n";

        testHandler(request, expected);
    }

    void testHandler(String request, String expectedResponse) {
        // given
        final var socket = new StubSocket(request);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        assertThat(socket.output()).isEqualTo(expectedResponse);
    }
}