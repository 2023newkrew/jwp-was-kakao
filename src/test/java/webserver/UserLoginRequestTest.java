package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;

public class UserLoginRequestTest {
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
        String[] outputs = socket.output().split("\r\n");

        assertThat(outputs[0]).isEqualTo("HTTP/1.1 200 OK ");
        assertThat(outputs[1]).isEqualTo("Content-Type: text/html;charset=utf-8 ");
        assertThat(outputs[2]).isEqualTo("Content-Length: 5026 ");
    }

    @Test
    @DisplayName("유저가 로그인된 상태에서 /user/login에 GET으로 접근하면 index.html로 리다이렉트 하는지 테스트")
    void loginedButUserLogin() {
        userRegisterRequest();
        String jSessionId = loginAndGetJSessionId();

        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/login.html HTTP/1.1",
                "Host: localhost:8080",
                "Cookie: JSESSIONID=" + jSessionId + "; Path=/",
                "Connection: keep-alive",
                "Accept: */*",
                "",
                "");

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

    private String loginAndGetJSessionId() {
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "userId=id&password=password");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();

        var output = socket.output();
        String[] splitOutput = output.split("\r\n");
        StringBuilder sessionId = new StringBuilder();
        Arrays.stream(splitOutput).forEach(line -> sessionId.append(getSetCookieString(line)));
        return sessionId.toString();
    }

    private String getSetCookieString(String outputLine) {
        if (outputLine.startsWith("Set-Cookie")) {
            return outputLine.split(";")[0].split("=")[1];
        }
        return "";
    }
}
