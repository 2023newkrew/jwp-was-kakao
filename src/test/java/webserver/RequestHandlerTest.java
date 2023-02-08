package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

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
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ ",
                "",
                "Hello world");

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
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
                "Content-Length: 6902 ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output().substring(0, 153))).isTrue();
        String indexHtml = new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));
        assertThat(socket.output().substring(153)).isEqualTo(indexHtml);
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
        var expected = String.join(CRLF, "HTTP/1.1 404 Not Found ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ ",
                "",
                "");

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
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
                "Content-Length: 7065 ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output().substring(0, 153))).isTrue();
        String css = new String(FileIoUtils.loadFileFromClasspath("static/css/styles.css"));
        assertThat(socket.output().substring(153)).isEqualTo(css);
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
                "Content-Length: 109518 ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output().substring(0, 155))).isTrue();
        String css = new String(FileIoUtils.loadFileFromClasspath("static/css/bootstrap.min.css"));
        assertThat(socket.output().substring(155)).isEqualTo(css);
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
        var expected = String.join(CRLF, "HTTP/1.1 404 Not Found ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ ", CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
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
                "Content-Length: 5168 ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output().substring(0, 153))).isTrue();
        String css = new String(FileIoUtils.loadFileFromClasspath("templates/user/form.html"));
        assertThat(socket.output().substring(153)).isEqualTo(css);
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
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ ",
                "Location: /index.html " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
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
        var expected = String.join(CRLF, "HTTP/1.1 400 Bad Request ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
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
        var expected = String.join(CRLF, "HTTP/1.1 400 Bad Request ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
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
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ ",
                "Location: /index.html " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
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
        var expected = String.join(CRLF, "HTTP/1.1 400 Bad Request ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
    }

    @Test
    void getLoginPage() throws IOException, URISyntaxException {
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
                "Content-Length: 4759 ",
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output().substring(0, 153))).isTrue();
        String css = new String(FileIoUtils.loadFileFromClasspath("templates/user/login.html"));
        assertThat(socket.output().substring(153)).isEqualTo(css);
    }

    @Test
    void cannotGetLoginPageWhenLogined() throws IOException, URISyntaxException {
        String jsessionId = login();
        // given
        final String httpRequest = String.join(CRLF,
                "GET /user/login.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=" + jsessionId,
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join(CRLF, "HTTP/1.1 302 Found ",
                "Location: /index.html " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
    }

    @DisplayName("로그인 후 리다이렉트 됐을 때 JSESSIONID가 쿠키에 실려서 요청되는지까지 테스트")
    @Test
    void loginSuccess() throws IOException, URISyntaxException {
        String jsessionId = createUser();
        // given
        final String httpRequest = String.join(CRLF,
                "POST /user/login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "Cookie: JSESSIONID=" + jsessionId,
                "",
                "userId=cu&password=password");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join(CRLF, "HTTP/1.1 302 Found ",
                "Location: /index.html " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();

        // given
        final String indexPageRequest = String.join(CRLF,
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=" + jsessionId,
                "",
                "");

        final var Othersocket = new StubSocket(indexPageRequest);
        final RequestHandler Otherhandler = new RequestHandler(Othersocket);

        // when
        Otherhandler.run();

        // then
        expected = String.join(CRLF, "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 6902 " + CRLF,
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html")));

        assertThat(Othersocket.output()).isEqualTo(expected);
    }

    @Test
    void loginFailCauseNotExistUserid() {
        String jsessionId = createUser();
        // given
        final String httpRequest = String.join(CRLF,
                "POST /user/login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "Cookie: JSESSIONID=" + jsessionId,
                "",
                "userId=abcd&password=password");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join(CRLF, "HTTP/1.1 302 Found ",
                "Location: /user/login_failed.html " + CRLF + CRLF);

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void loginFailCauseWrongPassword() {
        String jsessionId = createUser();
        // given
        final String httpRequest = String.join(CRLF,
                "POST /user/login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "Cookie: JSESSIONID=" + jsessionId,
                "",
                "userId=cu&password=wrongpassword");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join(CRLF, "HTTP/1.1 302 Found ",
                "Location: /user/login_failed.html " + CRLF + CRLF);

        assertThat(socket.output()).isEqualTo(expected);
    }

    String createUser() {
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
                "Set-Cookie: JSESSIONID=[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}; Path=/ ",
                "Location: /index.html " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();
        return socket.output().split("JSESSIONID=")[1].substring(0, 36);
    }

    String login() throws IOException, URISyntaxException {
        String jsessionId = createUser();
        // given
        final String httpRequest = String.join(CRLF,
                "POST /user/login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "Cookie: JSESSIONID=" + jsessionId,
                "",
                "userId=cu&password=password");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join(CRLF, "HTTP/1.1 302 Found ",
                "Location: /index.html " + CRLF + CRLF);

        assertThat(Pattern.matches(expected, socket.output())).isTrue();

        // given
        final String indexPageRequest = String.join(CRLF,
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=" + jsessionId,
                "",
                "");

        final var Othersocket = new StubSocket(indexPageRequest);
        final RequestHandler Otherhandler = new RequestHandler(Othersocket);

        // when
        Otherhandler.run();

        // then
        expected = String.join(CRLF, "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 6902 " + CRLF,
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html")));

        assertThat(Othersocket.output()).isEqualTo(expected);
        return jsessionId;
    }
}