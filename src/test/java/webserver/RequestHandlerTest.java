package webserver;

import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.IOUtils;
import web.RequestHandler;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    @Test
    void 기본_경로_접근_시_평문_응답이_반환된다() {
        // given
        final var socket = new StubSocket();
        final var handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Length: 11 ",
                "Content-Type: text/plain;charset=utf-8 ",
                "",
                "Hello world");

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void 인덱스_페이지_접근_시_해당_리소스가_반환된다() {
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
                new String(IOUtils.readFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void CSS_페이지_접근_시_해당_리소스가_반환된다() {
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
                "Content-Length: 7065 \r\n" +
                "Content-Type: text/css;charset=utf-8 \r\n" +
                "\r\n" +
                new String(IOUtils.readFileFromClasspath("static/css/styles.css"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void 회원가입_시_인덱스_페이지로_리다이렉트된다() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 92 ",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 302 Found ",
                "Location: /index.html "
        );

        assertThat(socket.output()).isEqualTo(expected);
    }
}
