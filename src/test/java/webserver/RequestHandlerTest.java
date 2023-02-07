package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @Test
    @DisplayName("주소가 '/'일때 Hello world를 출력하는지 테스트")
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
    @DisplayName("/index.html을 잘 불러오는지 테스트")
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
                "Content-Type: text/html \r\n" +
                "Content-Length: 7153 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("/user/form.html을 제대로 불러오는지 테스트")
    void userForm() throws URISyntaxException, IOException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/form.html HTTP/1.1 ",
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
                "Content-Type: text/html \r\n" +
                "Content-Length: 5297 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/user/form.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("회원가입 요청 후 리다이렉트를 잘 수행하는지 테스트")
    void register() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1",
                        "Host: localhost:8080",
                        "Connection: keep-alive",
                        "Content-Length: 59",
                        "Content-Type: application/x-www-form-urlencoded",
                        "Accept: */*",
                        "",
                        "userId=cu&password=password&name=abc&email=brainbackdoor%40gmail.com");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then

        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: http://localhost:8080/index.html \r\n"
                + "\r\n";

        assertThat(socket.output()).isEqualTo(expected);

    }
}
