package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestHeaderTest {
    @Test
    @DisplayName("header로 부터 요청 경로를 얻는다.")
    void extractRequestPathTest() {
        HttpRequestHeader header = new HttpRequestHeader(List.of("GET /index.html HTTP/1.1 "));
        String expected = "/index.html";
        assertThat(header.getRequestPath()).isEqualTo(expected);
    }

    @Test
    @DisplayName("header로 부터 HTTP 메서드 이름을 얻는다.")
    void extractRequestMethod() {
        HttpRequestHeader header = new HttpRequestHeader(List.of("GET /index.html HTTP/1.1 "));
        String expected = "GET";
        assertThat(header.getRequestMethod()).isEqualTo(expected);
    }

    @Test
    @DisplayName("쿠키를 포함한 header 생성 테스트")
    void createHeaderWithCookiesTest() {
        HttpRequestHeader header = new HttpRequestHeader(List.of("GET /index.html HTTP/1.1)",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Accept: */*",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "Cookie: Key=Value; Path=/key"));

        assertThat(header.getCookies()).hasSize(2);
        assertThat(header.getCookies()
                .get(0)
                .getCookieParam("JSESSIONID")).contains("656cef62-e3c4-40bc-a8df-94732920ed46");
    }

}
