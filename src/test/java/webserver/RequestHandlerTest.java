package webserver;

import db.DataBase;
import framework.RequestHandler;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import framework.utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @Test
    void 안녕세상() {
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
    void 없는_페이지_접근시_404_반환() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /no_such.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final var handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 404 Not Found ",
                "Content-Length: 0 ");

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void 인덱스_페이지_접근() throws IOException, URISyntaxException {
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
                "Content-Length: 6905 \r\n" +
                "\r\n" +
                new String(Objects.requireNonNull(FileIoUtils.loadFileFromClasspath("templates/index.html")));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void 유저를_생성할_수_있다() {
        // given
        final String httpBody = "userId=testtest&" +
                "password=passtest&" +
                "name=Sanghwa&" +
                "email=sss@ss.ss";
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: " + httpBody.getBytes().length,
                "",
                httpBody);
        final var socket = new StubSocket(httpRequest);
        final var handler = new RequestHandler(socket);
        int save = DataBase.findAll().size();

        // when
        handler.run();

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 302 Found ",
                "Location: /index.html ",
                "Content-Length: 0 ");

        assertThat(socket.output()).isEqualTo(expected);
        assertThat(DataBase.findAll().size()).isEqualTo(save + 1);
    }
}