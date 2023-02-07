package webserver;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
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
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 11 ",
                "",
                "Hello world");

        assertThat(socket.output()).isEqualTo(expected);
    }

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

    @Test
    void css() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /css/styles.css HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Accept: text/css,*/*;q=0.1",
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

    @Test
    void js() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /js/scripts.js HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Accept: */*",
                "Accept-Encoding: gzip, deflate",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then


        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: */*;charset=utf-8 \r\n" +
                "Content-Length: 226 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("static/js/scripts.js"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void font() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /fonts/glyphicons-halflings-regular.woff HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Accept: */*",
                "Accept-Encoding: gzip, deflate",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then


        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: */*;charset=utf-8 \r\n" +
                "Content-Length: 23424 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("static/fonts/glyphicons-halflings-regular.woff"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void createUser() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept-Encoding: gzip, deflate",
                "Connection: keep-alive ",
                "Upgrade-Insecure-Requests: 1",
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Content-Length: 57",
                "Accept-Language: en-US,en;q=0.9",
                "",
                "userId=test&password=test&name=test&email=test%40test.com");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then


        var expected = "HTTP/1.1 302 Found \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 0 \r\n" +
                "Location: /index.html \r\n" +
                "\r\n" +
                "";

        User expectedUser = new User("test", "test", "test", "test@test.com");
        User savedUser = DataBase.findUserById("test");

        assertThat(socket.output()).isEqualTo(expected);
        assertThat(savedUser.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(savedUser.getName()).isEqualTo(expectedUser.getName());
        assertThat(savedUser.getEmail()).isEqualTo(expectedUser.getEmail());
    }
}