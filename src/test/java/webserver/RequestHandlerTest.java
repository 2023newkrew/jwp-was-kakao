package webserver;

import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    private static final String CRLF = "\r\n";

    @Test
    void socketOut() {
        // given
        final var socket = new StubSocket();
        final var handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join(CRLF,
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
        final String httpRequest = String.join(CRLF,
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
        var expected = String.join(CRLF, "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 6902 " + CRLF,
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html")));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void wrongHtmlPath() {
        // given
        final String httpRequest = String.join(CRLF,
                "GET /indexx.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 404 Not Found " + CRLF + CRLF;

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void css() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join(CRLF,
                "GET ./css/styles.css HTTP/1.1",
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
        var expected = String.join(CRLF, "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 7065 " + CRLF,
                new String(FileIoUtils.loadFileFromClasspath("static/css/styles.css")));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void anotherCss() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join(CRLF,
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
        var expected = String.join(CRLF, "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 109518 " + CRLF,
                new String(FileIoUtils.loadFileFromClasspath("static/css/bootstrap.min.css")));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void wrongCssPath() {
        // given
        final String httpRequest = String.join(CRLF,
                "GET ./css/bootstrap..min.css HTTP/1.1",
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
        var expected = "HTTP/1.1 404 Not Found " + CRLF + CRLF;

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void form() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join(CRLF,
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
        var expected = String.join(CRLF, "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 5168 " + CRLF,
                new String(FileIoUtils.loadFileFromClasspath("templates/user/form.html")));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void createUserByGet() {
        // given
        final String httpRequest = String.join(CRLF,
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
        var expected = String.join(CRLF, "HTTP/1.1 302 Found ",
                "Location: /index.html " + CRLF + CRLF);

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void cannotCreateUserByGetWithWrongParameter() {
        // given
        final String httpRequest = String.join(CRLF,
                "GET /user/create?userId=cu&password=password&name=%EC=9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1",
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
        var expected = "HTTP/1.1 400 Bad Request " + CRLF + CRLF;

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void cannotCreateUserByGetWithWrongMethod() {
        // given
        final String httpRequest = String.join(CRLF,
                "GETT /user/create?userId=cu&password=password&name=%EC9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1",
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
        var expected = "HTTP/1.1 400 Bad Request " + CRLF + CRLF;

        assertThat(socket.output()).isEqualTo(expected);
    }


    @Test
    void createUserByPost() {
        // given
        final String httpRequest = String.join(CRLF,
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
        var expected = String.join(CRLF, "HTTP/1.1 302 Found ",
                "Location: /index.html " + CRLF + CRLF);

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void cannotCreateUserByPostWithWrongBodyQueryString() {
        // given
        final String httpRequest = String.join(CRLF,
                "POST /user/create HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 59",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "userId=cu&password=password&name=%E=C%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 400 Bad Request " + CRLF + CRLF;

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void login() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join(CRLF,
                "GET /user/login.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join(CRLF, "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 4759 " + CRLF,
                new String(FileIoUtils.loadFileFromClasspath("templates/user/login.html")));

        assertThat(socket.output()).isEqualTo(expected);
    }


}