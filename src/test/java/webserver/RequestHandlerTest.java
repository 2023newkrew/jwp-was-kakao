package webserver;

import application.controller.RootController;
import application.controller.UserController;
import application.db.DataBase;
import application.enums.ApplicationContentType;
import application.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;
import webserver.handler.Handlers;
import webserver.handler.resource.ResourceHandler;
import webserver.handler.resource.resolver.statics.StaticResolver;
import webserver.handler.resource.resolver.statics.StaticType;
import webserver.handler.resource.resolver.statics.StaticTypes;
import webserver.handler.resource.resolver.view.ViewResolver;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    private static final StaticTypes STATIC_TYPES = new StaticTypes(
            new StaticType("/css", ApplicationContentType.TEXT_CSS),
            new StaticType("/fonts", ApplicationContentType.FONT_TTF),
            new StaticType("/images", ApplicationContentType.IMAGE_PNG),
            new StaticType("/js", ApplicationContentType.TEXT_JAVASCRIPT)
    );

    private static final StaticResolver STATIC_RESOLVER = new StaticResolver(
            "./static",
            STATIC_TYPES,
            ApplicationContentType.TEXT_HTML
    );

    private static final ViewResolver VIEW_RESOLVER = new ViewResolver(
            "./templates",
            ApplicationContentType.TEXT_HTML
    );

    private static final Handlers HANDLERS = new Handlers(
            new RootController(),
            new UserController(),
            new ResourceHandler(STATIC_RESOLVER, VIEW_RESOLVER)
    );

    @Test
    void socket_out() {
        // given
        final var socket = new StubSocket();
        final var handler = new RequestHandler(socket, HANDLERS);

        // when
        handler.run();

        // then
        assertThat(socket.output())
                .contains("HTTP/1.1 200 OK")
                .contains("Content-Type: text/html;charset=utf-8")
                .contains("Content-Length: 11")
                .contains("Hello world");
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
        final var handler = new RequestHandler(socket, HANDLERS);
        // when
        handler.run();

        // then
        assertThat(socket.output())
                .contains("HTTP/1.1 200 OK")
                .contains("Content-Type: text/html;charset=utf-8")
                .contains("Content-Length: 6902")
                .contains(new String(FileIoUtils.loadFileFromClasspath("templates/index.html")));
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
        final var handler = new RequestHandler(socket, HANDLERS);

        // when
        handler.run();

        // then


        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/css;charset=utf-8 \r\n" +
                "Content-Length: 7065 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("static/css/styles.css"));

        assertThat(socket.output())
                .contains("HTTP/1.1 200 OK")
                .contains("Content-Type: text/css;charset=utf-8")
                .contains("Content-Length: 7065")
                .contains(new String(FileIoUtils.loadFileFromClasspath("static/css/styles.css")));
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
                "\r\nuserId=cu&password=password&name=name&email=brainbackdoor@gmail.com"
        );

        final var socket = new StubSocket(httpRequest);
        final var handler = new RequestHandler(socket, HANDLERS);

        // when
        handler.run();

        //then
        assertThat(DataBase.findUserById("cu"))
                .extracting(User::getName)
                .isEqualTo("name");

        assertThat(socket.output())
                .contains("HTTP/1.1 302 FOUND")
                .contains("Content-Length: 0")
                .contains("Location: /index.html");
    }
}