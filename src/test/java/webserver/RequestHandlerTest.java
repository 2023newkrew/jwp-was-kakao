package webserver;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;
import webserver.handler.Handlers;
import webserver.handler.ResourceHandler;
import webserver.handler.controller.RootController;
import webserver.handler.controller.UserController;
import webserver.handler.resolver.Resolvers;
import webserver.handler.resolver.resource.ResourceResolver;
import webserver.handler.resolver.view.ViewResolver;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    Resolvers resolvers = new Resolvers(
            new ResourceResolver(),
            new ViewResolver()
    );


    Handlers handlers = new Handlers(
            new RootController(),
            new UserController(),
            new ResourceHandler(resolvers)
    );

    @Test
    void socket_out() {
        // given
        final var socket = new StubSocket();
        final var handler = new RequestHandler(socket, handlers);

        // when
        handler.run();

        // then
        var expected = String.join(
                "\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 11 ",
                "",
                "Hello world"
        );

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join(
                "\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                ""
        );

        final var socket = new StubSocket(httpRequest);
        final var handler = new RequestHandler(socket, handlers);
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

    @DisplayName("CSS 지원하기")
    @Test
    void css() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join(
                "\r\n",
                "GET /css/styles.css HTTP/1.1",
                "Host: localhost:8080",
                "Accept: text/css,*/*;q=0.1",
                "Connection: keep-alive"
        );

        final var socket = new StubSocket(httpRequest);
        final var handler = new RequestHandler(socket, handlers);

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

    @DisplayName("회원가입")
    @Test
    void createUser() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join(
                "\r\n",
                "POST /user/create HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 59",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "\r\nuserId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com"
        );

        final var socket = new StubSocket(httpRequest);
        final var handler = new RequestHandler(socket, handlers);

        // when
        handler.run();

        assertThat(DataBase.findUserById("cu"))
                .extracting(User::getName)
                .isEqualTo("이동규");
    }
}