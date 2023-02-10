package webserver;

import db.DataBase;
import model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @Test
    void socketOutTest() {
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
    void indexTest() throws IOException, URISyntaxException {
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
                "Content-Length: 7149 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("GET 방식으로 form으로 부터 user 생성 테스트")
    void createUserByGetMethodTest() {
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

        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Location: http://localhost:8080/index.html \r\n" +
                "\r\n";

        assertThat(DataBase.findAll()).hasSize(1);
        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("POST 방식으로 form으로 부터 user 생성 테스트")
    void createUserByPostMethodTest() {
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 92 ",
                "Content-Type: text/plain ",
                "Accept: */*",
                "",
                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();

        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Location: http://localhost:8080/index.html \r\n" +
                "\r\n";

        assertThat(DataBase.findAll()).hasSize(1);
        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Post 방식으로 로그인 테스트")
    void loginUserByPostMethodTest() {
        User user = new User("cu", "password", "name", "email");
        DataBase.addUser(user);
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 27 ",
                "Content-Type: text/plain ",
                "Accept: */*",
                "",
                "userId=cu&password=password",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();

        String output = socket.output();

        System.out.println("output = " + output);
        assertThat(output).contains("Set-Cookie");
    }

    @Test
    @DisplayName("잘못된 queryParams가 들어오면 InvalidQueryParameterException 발생")
    void InvalidQueryParameterExceptionTest() {
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

        var expected = "HTTP/1.1 400 BAD_REQUEST \r\n" +
                "Content-Type: application/json;charset=utf-8 \r\n" +
                "Content-Length: 23 \r\n" +
                "\r\n" +
                "Invalid Query Parameter";

        assertThat(socket.output()).isEqualTo(expected);
    }
}
