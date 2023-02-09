package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RequestHeaderTest {

    @DisplayName("BufferedReader를 기반으로 Http Header를 추출할 수 있다.")
    @Test
    void parseTest() {
        // given
        String httpHeader = String.join("\r\n",
            "Host: localhost:8080 ",
            "Connection: keep-alive ",
            "Accept: */*",
            "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
            "",
            "");

        BufferedReader bufferedReader = new BufferedReader(new StringReader(httpHeader));

        // when
        assertThatCode(() -> RequestHeader.parse(bufferedReader))
            .doesNotThrowAnyException();
    }

    @DisplayName("Http Header에 저장된 쿠키 정보를 추출할 수 있다.")
    @Test
    void parseCookieTest() throws IOException {
        // given
        String userSessionId = "656cef62-e3c4-40bc-a8df-94732920ed46";
        String httpHeader = String.join("\r\n",
            "Host: localhost:8080 ",
            "Connection: keep-alive ",
            "Accept: */*",
            "Cookie: JSESSIONID=" + userSessionId + "; Path=/",
            "",
            "");

        BufferedReader bufferedReader = new BufferedReader(new StringReader(httpHeader));
        RequestHeader requestHeader = RequestHeader.parse(bufferedReader);

        // when
        HttpCookie cookie = requestHeader.getCookie();
        String sessionId = cookie.getSessionId();

        // then
        assertThat(sessionId).isEqualTo(userSessionId);
    }

    @DisplayName("ReqeustBody에 담긴 사이즈를 정보를 얻을 수 있다.")
    @Test
    void findRequestBodySize() throws IOException {
        // given
        String httpHeader =
            "Host: www.example.com\r\n" +
            "Content-Type: application/json\r\n" +
            "Content-Length: 26\r\n" +
            "\r\n" +
            "{\r\n" +
            "    \"message\": \"Hello World\"\r\n" +
            "}\r\n";

        BufferedReader bufferedReader = new BufferedReader(new StringReader(httpHeader));
        RequestHeader requestHeader = RequestHeader.parse(bufferedReader);

        // when
        int requestBodySize = requestHeader.findRequestBodySize();

        // then
        assertThat(requestHeader.checkRequestBodyExists()).isTrue();
        assertThat(requestBodySize).isEqualTo(26);
    }
}
