package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @Test
    @DisplayName("주소가 '/'일때 Hello world를 출력하는지 테스트")
    void socket_out() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET / HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

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
    @DisplayName("/index.html을 잘 불러오는지 테스트")
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then

        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html \r\n" +
                "Content-Length: 7153 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("/user/form.html을 제대로 불러오는지 테스트")
    void userForm() throws URISyntaxException, IOException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/form.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then

        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html \r\n" +
                "Content-Length: 5297 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/user/form.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("회원가입 요청 후 리다이렉트를 잘 수행하는지 테스트")
    void register() {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1",
                        "Host: localhost:8080",
                        "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                        "Connection: keep-alive",
                        "Content-Length: 66",
                        "Content-Type: application/x-www-form-urlencoded",
                        "Accept: */*",
                        "",
                        "userId=cu&password=password&name=abc&email=brainbackdoor@gmail.com\n");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then

        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: http://localhost:8080/index.html \r\n"
                + "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("로그인 페이지에서 로그인 성공 시 리다이렉트가 잘 이루어지는지 테스트")
    void loginSuccessRedirect() {
        userRegisterRequest();

        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1",
                "Host: localhost:8080",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "Connection: keep-alive",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "userId=id&password=password");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then

        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: http://localhost:8080/index.html \r\n"
                + "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }


    @Test
    @DisplayName("로그인 페이지에서 로그인 실패 시 리다이렉트가 잘 이루어지는지 테스트")
    void loginFailRedirect() {
        userRegisterRequest();

        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1",
                "Host: localhost:8080",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "Connection: keep-alive",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "userId=id&password=wrongPassword");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then

        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: http://localhost:8080/user/login_failed.html \r\n"
                + "\r\n";

        assertThat(socket.output()).isEqualTo(expected);

    }

    @Test
    @DisplayName("Request Header의 Cookie에 JSESSIONID가 없으면 Response Header에 Set-Cookie를 반환하는지 테스트")
    void cookieResponse() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET / HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var output = socket.output();
        String[] splitOutput = output.split("\r\n");
        boolean existSetCookie = false;
        for (int i = 1; i < splitOutput.length; i++) {
            if (splitOutput[i].startsWith("Set-Cookie")) {
                System.out.println(splitOutput[i]);
                existSetCookie = true;
                break;
            }
        }
        assertThat(existSetCookie).isEqualTo(true);
    }

    @Test
    @DisplayName("유저가 로그인 되어있을 때 유저 리스트를 잘 조회하는지 테스트")
    void userListLogined(){
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/list HTTP/1.1",
                "Host: localhost:8080",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/; logined=true",
                "Connection: keep-alive",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "userId=id&password=password");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        String firstLine = socket.output().split("\r\n")[0];
        assertThat(firstLine).isEqualTo("HTTP/1.1 200 OK ");
    }

    @Test
    @DisplayName("유저가 로그인 되어있지 않을 때 /user/list를 요청하면 로그인 페이지로 리다이렉트하는지 테스트")
    void userListNotLogined(){
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/list HTTP/1.1",
                "Host: localhost:8080",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "Connection: keep-alive",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "userId=id&password=password");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: http://localhost:8080/user/login.html \r\n"
                + "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    private void userRegisterRequest() {
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1",
                "Host: localhost:8080",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "Connection: keep-alive",
                "Content-Length: 66",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "userId=id&password=password&name=abc&email=brainbackdoor@gmail.com\n");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
    }

}

