package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;

class DefaultRequestHandleTest {
    @Test
    @DisplayName("주소가 '/'일때 Hello world를 출력하는지 테스트")
    void defaultRequest() {
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

        StringBuilder sessionId = new StringBuilder();
        for (int i = 1; i < splitOutput.length; i++) {
            sessionId.append(getSetCookieString(splitOutput[i]));
        }

        assertThat(!sessionId.toString().equals("")).isEqualTo(true);
    }
    private String getSetCookieString(String outputLine) {
        if (outputLine.startsWith("Set-Cookie")) {
            return outputLine.split(";")[0].split("=")[1];
        }
        return "";
    }
}
