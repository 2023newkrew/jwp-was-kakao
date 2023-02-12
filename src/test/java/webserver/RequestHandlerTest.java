package webserver;

import auth.SessionManager;
import db.DataBase;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    @DisplayName("인덱스 페이지에 잘 접근되는지 확인한다.")
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

    @DisplayName("css파일이 잘 로드되는지 확인한다.")
    @Test
    void css() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /css/styles.css HTTP/1.1 ",
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
                "Content-Type: text/css;charset=utf-8 \r\n" +
                "Content-Length: 7065 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("static/css/styles.css"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("회원가입이 정상적으로 되는지 확인한다.")
    @Test
    void createUser() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 92",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        User expected = new User(
                "cu",
                "password",
                "%EC%9D%B4%EB%8F%99%EA%B7%9C",
                "brainbackdoor%40gmail.com"
        );

        assertThat(DataBase.findAll().toArray()[0].toString()).isEqualTo(expected.toString());
    }

    @DisplayName("회원가입 이후 인덱스페이지로 리다이렉트되는지 확인한다.")
    @Test
    void createUserRedirect() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 92",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();
        // then
        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: " + "/index.html" + " \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("로그인했을 때 세션이 정상적으로 생성되는지 확인한다.")
    @Test
    void createUserSession() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 92",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com");
        final String httpRequest2 = String.join("\r\n",
                "POST /user/login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                "userId=cu&password=password");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        final var socket2 = new StubSocket(httpRequest2);
        final RequestHandler handler2 = new RequestHandler(socket2);

        // when
        handler.run();
        handler2.run();

        // then
        Set set = SessionManager.keySet();
        Iterator iterator = set.iterator();
        User expected = SessionManager.findSession((String) iterator.next()).getAttribute("userObject");

        assertThat(DataBase.findAll().toArray()[0].toString()).isEqualTo(expected.toString());
    }

    @DisplayName("로그인한 상태에서 로그인버튼을 누르면 인덱스페이지로 리다이렉트되는지 확인한다.")
    @Test
    void loginRedirect() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 92",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com");
        final String httpRequest2 = String.join("\r\n",
                "POST /user/login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                "userId=cu&password=password");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        final var socket2 = new StubSocket(httpRequest2);
        final RequestHandler handler2 = new RequestHandler(socket2);

        handler.run();
        handler2.run();

        Set set = SessionManager.keySet();
        Iterator iterator = set.iterator(); // 쿠키 값

        final String httpRequest3 = String.join("\r\n",
                "GET /user/login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=" + iterator.next());

        final var socket3 = new StubSocket(httpRequest3);
        final RequestHandler handler3 = new RequestHandler(socket3);

        // when
        handler3.run();

        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: " + "/index.html" + " \r\n" +
                "\r\n";

        // then
        assertThat(socket.output()).isEqualTo(expected);
    }
}