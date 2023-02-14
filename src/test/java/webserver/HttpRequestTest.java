package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {
    @DisplayName("HttpRequest 파싱")
    @Test
    void parse() {
        final var requestBody = "userId=cu" +
                "&password=password" +
                "&name=%EC%9D%B4%EB%8F%99%EA%B7%9C" +
                "email=brainbackdoor%40gmail.com";
        final var request = "POST /user/create HTTP/1.1 \r\n" +
                "Host: localhost:8080 \r\n" +
                "Connection: keep-alive \r\n" +
                "Content-Length: " + requestBody.getBytes().length + " \r\n" +
                "Content-Type: application/x-www-form-urlencoded \r\n" +
                "Accept: */* \r\n" +
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46, aaa=111 \r\n" +
                "\r\n" +
                requestBody;

        HttpRequest httpRequest = new HttpRequest(new ByteArrayInputStream(request.getBytes()));

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getUrl()).isEqualTo("/user/create");
        assertThat(httpRequest.getHeader("Host")).isEqualTo("localhost:8080");
        assertThat(httpRequest.getParameter("userId")).isEqualTo("cu");
        assertThat(httpRequest.getCookie("JSESSIONID")).isEqualTo("656cef62-e3c4-40bc-a8df-94732920ed46");
    }
}