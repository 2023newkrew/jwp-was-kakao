package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;

public class UserRegisterRequestHandleTest {
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
    @DisplayName("회원가입 시 존재하는 ID면 회원가입 실패 페이지로 리다이렉트하는지 테스트")
    void userRegisterButAlreadyExistID() {
        userRegisterRequest();

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
                "userId=id&password=password&name=abc&email=brainbackdoor@gmail.com\n");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 Found \r\n" +
                "Location: http://localhost:8080/user/register_failed.html \r\n"
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
