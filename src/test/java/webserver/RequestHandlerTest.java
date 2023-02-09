package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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
        var expected = List.of("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Length: 11 ",
                "Content-Type: text/html;charset=utf-8 ",
                "",
                "Hello world");

        assertThat(socket.output()).contains(expected);
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
                "Content-Length: 6897 \r\n" +
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
                "Content-Type: text/css;charset=utf-8 \r\n" +
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
        var expected = List.of("HTTP/1.1 200 OK \r\n",
                "Content-Length: 109518 \r\n",
                "Content-Type: text/css;charset=utf-8 \r\n",
                "\r\n",
                new String(FileIoUtils.loadFileFromClasspath("static/css/bootstrap.min.css")));

        assertThat(socket.output()).contains(expected);
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
        var expected = List.of("HTTP/1.1 200 OK \r\n",
                "Content-Type: text/html;charset=utf-8 \r\n",
                "Content-Length: 5162 \r\n",
                "\r\n",
                new String(FileIoUtils.loadFileFromClasspath("templates/user/form.html")));

        assertThat(socket.output()).contains(expected);
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
                "Content-Length: 92",
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

        assertThat(socket.output()).contains(expected);
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

        assertThat(socket.output()).contains(expected);
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

        assertThat(socket.output()).contains(expected);
    }

    @DisplayName("요구되는 파라미터가 요청에 없는 경우 400이 반환된다")
    @Test
    void missingParameter() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 46",
                "Accept: */*",
                "",
                "userId=subin&name=subin&email=subin@google.com");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 400 Bad Request \r\n" +
                "\r\n";

        assertThat(socket.output()).contains(expected);
    }

    @DisplayName("요구되는 파라미터가 빈 문자열이나 공백의 문자열일 경우 400이 반환된다")
    @Test
    void blankParameter() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 56",
                "Accept: */*",
                "",
                "userId=subin&password= &name=subin&email=subin@google.com");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 400 Bad Request \r\n" +
                "\r\n";

        assertThat(socket.output()).contains(expected);
    }

    @DisplayName("로그인 실패한 사용자는 실패 페이지로 리다이렉트된다")
    @Test
    void loginFailed() {
        // given
        String userId = "subin";
        String password = "asdfczxi";
        createUserForTest(userId, password);

        String parameter = "userId="+userId+"&password="+password+"not_equal!";
        final String httpRequest = String.join("\r\n",
                "POST /user/login  HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: "+parameter.length(),
                "Accept: */*",
                "",
                parameter);

        var socket = new StubSocket(httpRequest);
        var handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: /user/login_failed.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("로그인 성공한 사용자는 쿠키가 설정되어있지 않으면 응답을 통해 쿠키가 설정된다")
    @Test
    void loginSucceed() {
        // given
        String userId = "subin";
        String password = "asdfczxi";
        createUserForTest(userId, password);

        String parameter = "userId="+userId+"&password="+password;
        final String httpRequest = String.join("\r\n",
                "POST /user/login  HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: "+parameter.length(),
                "Accept: */*",
                "",
                parameter);

        var socket = new StubSocket(httpRequest);
        var handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: /index.html \r\n" +
                "Set-Cookie: ";

        assertThat(socket.output()).contains(expected);
    }

    private void createUserForTest(String userId, String password) {
        // given
        String parameter = "userId="+userId+"&password="+password+"&name=subin&email=subin@google.com";
        final String setUpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: "+parameter.length(),
                "Accept: */*",
                "",
                parameter);

        var socket = new StubSocket(setUpRequest);
        RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        assertThat(socket.output()).contains("Found");
    }
}