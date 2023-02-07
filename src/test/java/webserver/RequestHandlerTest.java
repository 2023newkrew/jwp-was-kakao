package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import db.DataBase;
import java.io.IOException;
import java.net.URISyntaxException;
import model.User;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

class RequestHandlerTest {

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
                "Content-Length: 7153 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void createUser() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1\n" +
                        "Host: localhost:8080\n" +
                        "Connection: keep-alive\n" +
                        "Content-Length: 92\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Accept: */*\n" +
                        "\n" +
                        "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com\n");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        User user = DataBase.findUserById("cu");
        assertThat(user).hasFieldOrPropertyWithValue("password", "password")
                .hasFieldOrPropertyWithValue("name", "이동규")
                .hasFieldOrPropertyWithValue("email", "brainbackdoor@gmail.com");
        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Location: http://localhost:8080/index.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }
}