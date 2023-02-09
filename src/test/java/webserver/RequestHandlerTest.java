package webserver;

import db.DataBase;
import java.util.List;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import webserver.controller.Controller;
import webserver.controller.HelloWorldController;
import webserver.controller.LoginController;
import webserver.controller.PostController;
import webserver.controller.QueryStringController;
import webserver.controller.UserCreateController;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    private static final List<Controller> CONTROLLERS = List.of(
            new HelloWorldController(),
            new PostController(),
            new QueryStringController(),
            new UserCreateController(),
            new LoginController()
    );

    @Test
    void socket_out() {
        // given
        final var httpRequest = String.join("\r\n",
                "GET / HTTP/1.1",
                "Accept: text/html",
                "Host: localhost:8080",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final var handler = new RequestHandler(socket, CONTROLLERS);

        // when
        handler.run();

        // then

        assertThat(socket.output().split("\r\n")).contains(
                "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 11 ",
                "",
                "Hello world"
        );
    }

    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Accept: text/html",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, CONTROLLERS);

        // when
        handler.run();

        // then
        var indexHtml = FileIoUtils.loadFileFromClasspath("templates/index.html");

        assertThat(socket.output().split("\r\n")).contains(
                "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: " + indexHtml.length + " ",
                "",
                new String(indexHtml)
        );
    }

    @Test
    void css() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /css/styles.css HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Accept: text/css,*/*;q=0.1 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, CONTROLLERS);

        // when
        handler.run();

        // then
        var cssFile = FileIoUtils.loadFileFromClasspath("static/css/styles.css");

        assertThat(socket.output().split("\r\n")).contains(
                "HTTP/1.1 200 OK ",
                "Content-Type: text/css;charset=utf-8 ",
                "Content-Length: " + cssFile.length + " ",
                "",
                new String(cssFile)
        );
    }

    @Test
    void querystring() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /query?name=" + URLEncoder.encode("케인", Charset.defaultCharset()) + " HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Accept: text/plain",
                "Connection: keep-alive ",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, CONTROLLERS);

        // when
        handler.run();

        // then
        assertThat(socket.output().split("\r\n")).contains(
                "HTTP/1.1 200 OK ",
                "Content-Type: text/plain;charset=utf-8 ",
                "Content-Length: 12 ",
                "hello 케인"
        );
    }

    @Test
    void requestBody() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /post HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Accept: text/plain",
                "Connection: keep-alive ",
                "Content-Length: 9",
                "Content-Type: application/x-www-form-urlencoded",
                "",
                "name=kane");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, CONTROLLERS);

        // when
        handler.run();

        // then
        assertThat(socket.output().split("\r\n")).contains(
                "HTTP/1.1 200 OK ",
                "Content-Length: 10 ",
                "Content-Type: text/plain;charset=utf-8 ",
                "hello kane"
        );
    }

    @Test
    void userCreate() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1",
                "Host: localhost:8080 ",
                "Accept: text/plain",
                "Connection: keep-alive ",
                "Content-Length: 92",
                "Content-Type: application/x-www-form-urlencoded",
                "",
                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, CONTROLLERS);

        // when
        handler.run();

        // then
        assertThat(socket.output().split("\r\n")).contains(
                "HTTP/1.1 302 Found ",
                "Location: /index.html "
        );

        User user = DataBase.findUserById("cu");
        assertThat(user.getUserId()).isEqualTo("cu");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("이동규");
        assertThat(user.getEmail()).isEqualTo("brainbackdoor@gmail.com");
    }

    @Test
    void login() {
        // given
        DataBase.addUser(new User("userId", "password", "name", "email"));

        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1",
                "Host: localhost:8080 ",
                "Accept: text/plain",
                "Connection: keep-alive ",
                "Content-Length: 31",
                "Content-Type: application/x-www-form-urlencoded",
                "",
                "userId=userId&password=password");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, CONTROLLERS);

        // when
        handler.run();

        // then
        assertThat(socket.output().split("\r\n")).contains(
                "HTTP/1.1 302 Found ",
                "Location: /index.html "
        );
    }

    @Test
    void loginFailedByInvalidPassword() {
        // given
        DataBase.addUser(new User("userId", "password", "name", "email"));

        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1",
                "Host: localhost:8080 ",
                "Accept: text/plain",
                "Connection: keep-alive ",
                "Content-Length: 31",
                "Content-Type: application/x-www-form-urlencoded",
                "",
                "userId=userId&password=wrong_password");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, CONTROLLERS);

        // when
        handler.run();

        // then
        assertThat(socket.output().split("\r\n")).contains(
                "HTTP/1.1 302 Found ",
                "Location: /user/login_failed.html "
        );
    }

    @Test
    void loginFailedByNotFoundUser() {
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1",
                "Host: localhost:8080 ",
                "Accept: text/plain",
                "Connection: keep-alive ",
                "Content-Length: 31",
                "Content-Type: application/x-www-form-urlencoded",
                "",
                "userId=userId&password=password");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, CONTROLLERS);

        // when
        handler.run();

        // then
        assertThat(socket.output().split("\r\n")).contains(
                "HTTP/1.1 302 Found ",
                "Location: /user/login_failed.html "
        );
    }
}