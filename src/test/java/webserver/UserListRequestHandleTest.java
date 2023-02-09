package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;

public class UserListRequestHandleTest {
    @Test
    @DisplayName("유저가 로그인 되어있을 때 유저 리스트를 잘 조회하는지 테스트")
    void userListLogined(){
        userRegisterRequest();
        String jSessionId = loginAndGetJSessionId();

        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/list HTTP/1.1",
                "Host: localhost:8080",
                "Cookie: JSESSIONID=" + jSessionId + "; Path=/",
                "Connection: keep-alive",
                "Content-Length: 27",
                "Content-Type: application/x-www-form-urlencoded",
                "Accept: */*",
                "",
                "");

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
        for (int i = 1; i < splitOutput.length; i++) {
            if (splitOutput[i].startsWith("Set-Cookie")) {
                return splitOutput[i].split(";")[0].split("=")[1];
            }
        }
        return null;
    }
}
