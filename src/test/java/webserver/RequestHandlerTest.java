package webserver;

import db.DataBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;
import utils.IOUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

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
                "Content-Length: 6943 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("InputStream으로 부터 requestFirstLine를 얻는다.")
    void extractRequestFirstLineTest(){
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.UTF_8));
        String path = IOUtils.extractRequestFirstLine(inputStream);
        assertThat(path).isEqualTo("GET /index.html HTTP/1.1 ");
    }

    @Test
    @DisplayName("requestFirstLine로부터 요청 경로를 얻는다.")
    void extractPathTest(){
        String requestFirstLine = "GET /index.html HTTP/1.1 ";
        String expected = "/index.html";
        assertThat(IOUtils.extractPath(requestFirstLine)).isEqualTo(expected);
    }

    @Test
    @DisplayName("form으로 부터 user 생성 테스트")
    void createUserTest(){
        final String httpRequest = String.join("\r\n",
                "GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Accept: */*",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();

        assertThat(DataBase.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("잘못된 queryParams가 들어오면 InvalidQueryParameterException 발생")
    void InvalidQueryParameterExceptionTest(){
        final String httpRequest = String.join("\r\n",
                "GET /user/createuserId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Accept: */*",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();

        var expected = "HTTP/1.1 400 BAD REQUEST \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }
}