package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @DisplayName("루트 요청시 Hello world를 응답한다")
    @Test
    void socketOut() {
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

    @DisplayName("HTML 파일 요청시 해당 파일을 읽어 응답한다")
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
                "Content-Length: 6902 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("CSS 파일 요청시 해당 파일을 읽어 응답한다")
    @Test
    void css() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /css/styles.css HTTP/1.1",
                "Host: localhost:8080",
                "Accept: text/css,*/*;q=0.1",
                "Connection: keep-alive",
                "",
                "");


        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 7065 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("static/css/styles.css"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("파일 요청시 현재 위치(.)를 포함해도 파일을 읽어 응답할 수 있다")
    @Test
    void anotherCss() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET ./css/bootstrap.min.css HTTP/1.1",
                "Host: localhost:8080",
                "Accept: text/css,*/*;q=0.1",
                "Connection: keep-alive",
                "",
                "");


        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 109518 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("static/css/bootstrap.min.css"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("파일 요청에 특정 API 경로가 포함되어도 파일 응답으로 처리된다")
    @Test
    void form() throws IOException, URISyntaxException {
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
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 5167 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/user/form.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("GET 요청의 쿼리 파라미터를 통해 사용자를 생성할 수 있다")
    @Test
    void createUserByGet() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Accept: */*",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: /index.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("POST 요청의 바디 파라미터를 통해 사용자를 생성할 수 있다")
    @Test
    void createUserByPost() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 59",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: /index.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 경로로 요청할 경우 404가 응답된다")
    @Test
    void uriNotFound() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/not/exist/path HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Accept: */*",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 404 Not Found \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 메서드로 요청할 경우 400가 응답된다")
    @Test
    void methodNotExist() {
        // given
        final String httpRequest = String.join("\r\n",
                "ABCD /user/create HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Accept: */*",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 400 Bad Request \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("해당 경로에 존재하지 않는 메서드로 요청할 경우 405가 응답된다")
    @Test
    void methodNotSupported() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /index.html HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Accept: */*",
                "",
                "key=value");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 405 Method Not Allowed \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("지원하지 않는 바디 형식의 POST 요청은 415가 반환된다")
    @Test
    void statusCodeNoDefined() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /index.html HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Accept: */*",
                "",
                "{",
                "field: value",
                "}");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 415 Unsupported Media Type \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }
}