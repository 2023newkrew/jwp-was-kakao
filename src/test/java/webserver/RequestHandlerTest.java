package webserver;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import model.User;
import model.http.CustomHttpSession;
import model.http.SessionManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestHandlerTest {
    @DisplayName("기본 소켓 통신 테스트")
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
                "Content-Length: 11 ",
                "Content-Type: text/html;charset=utf-8 ",
                "",
                "Hello world");

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("인덱스 html 테스트")
    @Test
    void index() {
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
                "Content-Length: 6902 \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("css 테스트")
    @Test
    void css() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /css/styles.css HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Accept: text/css,*/*;q=0.1 ",
                "Connection: keep-alive ",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Length: 7065 \r\n" +
                "Content-Type: text/css;charset=utf-8 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("static/css/styles.css"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("회원 가입 GET 방식 테스트")
    @Test
    void addUserWithGetMethod() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1" +
                        "Host: localhost:8080" +
                        "Connection: keep-alive" +
                        "Accept: */*");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Location: /index.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("회원 가입 POST 방식 테스트")
    @Test
    void addUserWithPostMethod() {
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
        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Location: /index.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    void loginSuccess() {
        // given
        join();
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1",
                        "Host: localhost:8080",
                        "Connection: keep-alive",
                        "Content-Length: 27",
                        "Content-Type: application/x-www-form-urlencoded",
                        "Accept: */*",
                        "",
                        "userId=cu&password=password");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var output = socket.output();
        var prefix = "HTTP/1.1 302 FOUND \r\n" +
                "Set-Cookie: JSESSIONID=";
        var suffix = "; Path=/ \r\n" +
                "Location: /index.html \r\n" +
                "\r\n";

        assertTrue(output.startsWith(prefix) && output.endsWith(suffix));
    }

    @DisplayName("세션에 저장하는 회원 정보 테스트")
    @Test
    void sessionGetAttribute() {
        // given
        join();
        String jSessionId = login();

        // when
        CustomHttpSession session = SessionManager.getInstance().findSession(jSessionId).get();
        User loginUser = (User) session.getAttribute("loginUser");

        // then
        assertThat(loginUser.getUserId()).isEqualTo("cu");
    }

    @DisplayName("로그인 실패 테스트")
    @Test
    void loginFail() {
        // given
        join();
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1",
                        "Host: localhost:8080",
                        "Connection: keep-alive",
                        "Content-Length: 28",
                        "Content-Type: application/x-www-form-urlencoded",
                        "Accept: */*",
                        "",
                        "userId=cu&password=password2");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Location: /user/login_failed.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("유저 목록 조회 성공 테스트")
    @Test
    void userListSuccess() throws IOException {
        // given
        join();
        String jSessionId = login();
        final String httpRequest = String.join("\r\n",
                "GET /user/list HTTP/1.1",
                        "Host: localhost:8080",
                        "Connection: keep-alive",
                        "Content-Type: application/x-www-form-urlencoded",
                        "Cookie: JSESSIONID=" + jSessionId,
                        "Accept: */*");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline(new String(FileIoUtils.loadFileFromClasspath("templates/user/list.html")));
        String page = template.apply(List.of(new User("cu", "password", "%EC%9D%B4%EB%8F%99%EA%B7%9C", "brainbackdoor%40gmail.com")));

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "\r\n" +
                page;

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("유저 목록 조회 실패 테스트")
    @Test
    void userListFail() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/list HTTP/1.1",
                        "Host: localhost:8080",
                        "Connection: keep-alive",
                        "Content-Type: application/x-www-form-urlencoded",
                        "Accept: */*");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Location: /user/login.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    void join() {
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
        handler.run();
    }

    String login() {
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "userId=cu&password=password");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);
        handler.run();
        return socket.output().split("JSESSIONID=")[1].split(";")[0];
    }

}
